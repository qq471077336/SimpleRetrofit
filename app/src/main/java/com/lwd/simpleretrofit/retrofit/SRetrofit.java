package com.lwd.simpleretrofit.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @AUTHOR lwd
 * @TIME 2020/11/17
 * @DESCRIPTION TODO
 */
public class SRetrofit {

    private Map<Method, ServiceMethod> mServiceMethodMap = new ConcurrentHashMap<>();
    public final HttpUrl httpUrl;
    public final Call.Factory callFactory;

    public SRetrofit(Call.Factory callFactory, HttpUrl httpUrl) {
        this.httpUrl = httpUrl;
        this.callFactory = callFactory;
    }

    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        return serviceMethod.invoke(args);
                    }
                });
    }

    public ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = mServiceMethodMap.get(method);
        if (serviceMethod != null) {
            return serviceMethod;
        }
        synchronized (mServiceMethodMap) {
            serviceMethod = mServiceMethodMap.get(method);
            if (serviceMethod == null) {
                serviceMethod = new ServiceMethod.Builder(SRetrofit.this, method).build();
                mServiceMethodMap.put(method, serviceMethod);
            }
        }
        return serviceMethod;
    }

  public  static final class Builder{
      private HttpUrl httpUrl;
      private Call.Factory callFactory;

      public Builder callFactory(Call.Factory callFactory) {
          this.callFactory = callFactory;
          return this;
      }

      public Builder baseUrl(String baseUrl) {
          this.httpUrl = HttpUrl.get(baseUrl);
          return this;
      }


      public SRetrofit build(){
          if (this.httpUrl == null) {
              throw new IllegalStateException("httpUrl could not be null");
          }

          if (callFactory == null) {
              this.callFactory = new OkHttpClient();
          }

          return new SRetrofit(callFactory, httpUrl);
      }
  }


}
