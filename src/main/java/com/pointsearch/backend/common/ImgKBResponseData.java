package com.pointsearch.backend.common;


public class ImgKBResponseData {
    private int code;

    private String msg;

    private Data data;

    private long time;

    // Getters and setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class Data {
        private String id;

        private String name;

        private String url;

        private int size;

        private String mime;

        private String sha1;

        private String md5;

        private String quota;

        private String use_quota;

        // Getters and setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getQuota() {
            return quota;
        }

        public void setQuota(String quota) {
            this.quota = quota;
        }

        public String getUse_quota() {
            return use_quota;
        }

        public void setUse_quota(String use_quota) {
            this.use_quota = use_quota;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", size=" + size +
                    ", mime='" + mime + '\'' +
                    ", sha1='" + sha1 + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", quota='" + quota + '\'' +
                    ", use_quota='" + use_quota + '\'' +
                    '}';
        }
    }

}