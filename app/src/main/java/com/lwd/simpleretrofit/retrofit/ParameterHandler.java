package com.lwd.simpleretrofit.retrofit;

/**
 * @AUTHOR lwd
 * @TIME 2020/11/19
 * @DESCRIPTION TODO
 */
public abstract class ParameterHandler {

    public abstract void apply(ServiceMethod serviceMethod, String value);

    static class FieldParameterHandler extends ParameterHandler {

        private final String key;

        public FieldParameterHandler(String key) {
            this.key = key;
        }

        @Override
        public void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addFieldParameter(key, value);
        }
    }

    static class QueryParameterHandler extends ParameterHandler {

        private final String key;

        public QueryParameterHandler(String key) {
            this.key = key;
        }

        @Override
        public void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParameter(key, value);
        }
    }
}
