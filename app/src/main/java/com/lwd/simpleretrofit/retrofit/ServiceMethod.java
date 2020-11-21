package com.lwd.simpleretrofit.retrofit;

import android.text.TextUtils;

import com.lwd.simpleretrofit.retrofit.annotion.Field;
import com.lwd.simpleretrofit.retrofit.annotion.GET;
import com.lwd.simpleretrofit.retrofit.annotion.POST;
import com.lwd.simpleretrofit.retrofit.annotion.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @AUTHOR lwd
 * @TIME 2020/11/17
 * @DESCRIPTION TODO
 */
public class ServiceMethod {

    private final HttpUrl mHttpUrl;
    private final Call.Factory mCallFactory;
    private final String mHttpMethod;
    private final String mRelativeUrl;
    private final boolean mHasBody;
    private final ParameterHandler[] mParameterHandlers;
    private FormBody.Builder mFormBuilder;
    private HttpUrl.Builder mHttpBuilder;

    public ServiceMethod(Builder builder) {
        mHttpUrl = builder.mSRetrofit.httpUrl;
        mCallFactory = builder.mSRetrofit.callFactory;
        mHttpMethod = builder.mHttpMethod;
        mRelativeUrl = builder.mRelativeUrl;
        mHasBody = builder.hasBody;
        mParameterHandlers = builder.mParameterHandlers;

        if (mHasBody) {
            mFormBuilder = new FormBody.Builder();
        }

    }

    public Object invoke(Object[] args) {
        for (int i = 0; i < mParameterHandlers.length; i++) {
            ParameterHandler parameterHandler = mParameterHandlers[i];
            parameterHandler.apply(this, args[i].toString());
        }
        HttpUrl url = null;
        if (mHttpBuilder != null) {
            url = mHttpBuilder.build();
        }
        FormBody formBody = null;
        if (mFormBuilder != null) {
            formBody = mFormBuilder.build();
        }
        Request request = new Request.Builder().url(url).method(mHttpMethod, formBody).build();

        return mCallFactory.newCall(request);
    }

    public void addFieldParameter(String key, String value) {
        if (mFormBuilder == null) {
            mFormBuilder = new FormBody.Builder();
        }
        mFormBuilder.add(key, value);

    }

    public void addQueryParameter(String key, String value) {

        if (mHttpBuilder == null) {
            mHttpBuilder = mHttpUrl.newBuilder(mRelativeUrl);
        }
        mHttpBuilder.addQueryParameter(key, value);
    }

    public static final class Builder {

        private final SRetrofit mSRetrofit;
        private final Annotation[] mMethodAnnotations;
        private final Annotation[][] mParameterAnnotations;
        private String mHttpMethod;
        private String mRelativeUrl;
        private boolean hasBody;

        ParameterHandler[] mParameterHandlers;

        public Builder(SRetrofit sRetrofit, Method method) {
            this.mSRetrofit = sRetrofit;
            mMethodAnnotations = method.getAnnotations();
            mParameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            for (Annotation methodAnnotation : mMethodAnnotations) {
                if (!TextUtils.isEmpty(mHttpMethod)) {
                    throw new IllegalStateException("");
                }
                if (methodAnnotation instanceof POST) {
                    mHttpMethod = "POST";
                    mRelativeUrl = ((POST) methodAnnotation).value();
                    hasBody = true;
                } else if (methodAnnotation instanceof GET) {
                    mHttpMethod = "GET";
                    mRelativeUrl = ((GET) methodAnnotation).value();
                    hasBody = false;
                }
            }

            int length = mParameterAnnotations.length;
            mParameterHandlers = new ParameterHandler[length];

            for (int i = 0; i < length; i++) {
                for (Annotation annotation : mParameterAnnotations[i]) {
                    if (annotation instanceof Field) {
                        String value = ((Field) annotation).value();
                        mParameterHandlers[i] = new ParameterHandler.FieldParameterHandler(value);
                    } else if (annotation instanceof Query) {
                        String value = ((Query) annotation).value();
                        mParameterHandlers[i] = new ParameterHandler.QueryParameterHandler(value);
                    }
                }
            }

            return new ServiceMethod(this);
        }
    }
}
