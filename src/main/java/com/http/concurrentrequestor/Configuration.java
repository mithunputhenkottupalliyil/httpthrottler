package com.http.concurrentrequestor;

import com.http.concurrentrequestor.config.Config;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class Configuration {
    @Autowired
    Config config;

    @Bean
    public CloseableHttpClient getClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(config.getConnectionPoolAttributes().getConnectionReqTimeout())
                .setConnectTimeout(config.getConnectionPoolAttributes().getConnectionTimeOut()).build();

        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(getPoolingConnectionManager())
                .setKeepAliveStrategy(getKeepAliveStrategy())
                .setServiceUnavailableRetryStrategy(getServiceRetryStrategy())
                .build();
    }

    private ConnectionKeepAliveStrategy getKeepAliveStrategy() {
        return (response, httpContext) -> {
            HeaderElementIterator iter = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while(iter.hasNext()){
                HeaderElement he = iter.nextElement();
                String value = he.getValue();
                if(value != null && "timeout".equalsIgnoreCase(he.getName())){
                    return Long.parseLong(value);
                }
            }
            return config.getConnectionPoolAttributes().getDefaultKeepAlive();
        };
    }

    private ServiceUnavailableRetryStrategy getServiceRetryStrategy() {

        return new ServiceUnavailableRetryStrategy() {
            @Override
            public boolean retryRequest(HttpResponse httpResponse, int retryCount, HttpContext httpContext) {
                return httpResponse.getStatusLine().getStatusCode() == 429 && retryCount < 2;
            }

            @Override
            public long getRetryInterval() {
                return config.getConnectionPoolAttributes().getRetryAfter();
            }
        };
    }

    private HttpClientConnectionManager getPoolingConnectionManager() {
        Registry<ConnectionSocketFactory> connectionSocketFactoryRegistry;
        try{
             connectionSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory()).build();
        }catch (Exception e){
            throw new RuntimeException("Connection Factory Setup failed", e);
        }
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(connectionSocketFactoryRegistry);
        connectionManager.setMaxTotal(config.getConnectionPoolAttributes().getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(config.getConnectionPoolAttributes().getDefaultMaxPerRoute());
        return connectionManager;
    }
}
