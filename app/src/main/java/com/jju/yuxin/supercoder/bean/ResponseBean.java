package com.jju.yuxin.supercoder.bean;

import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.bean
 * Created by yuxin.
 * Created time 2016/12/19 0019 下午 9:06.
 * Version   1.0;
 * Describe :  图片请求bean
 * History:
 * ==============================================================================
 */

public class ResponseBean {
    private int id;
    private int bytes;
    private String created_at;
    /**
     * url : http://a.desktopprassets.com/wallpapers/47949f0c69ae431bc7526adcf526a62e74cce796/alienage.jpg
     * thumb : {"url":"http://a.desktopprassets.com/wallpapers/47949f0c69ae431bc7526adcf526a62e74cce796/thumb_alienage.jpg","width":296,"height":185}
     * preview : {"url":"http://a.desktopprassets.com/wallpapers/47949f0c69ae431bc7526adcf526a62e74cce796/preview_alienage.jpg","width":960,"height":600}
     */

    private ImageBean image;
    private int height;
    private int width;
    private String review_state;
    private String uploader;
    private int user_count;
    private int likes_count;
    private String url;
    private List<String> palette;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getReview_state() {
        return review_state;
    }

    public void setReview_state(String review_state) {
        this.review_state = review_state;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getPalette() {
        return palette;
    }

    public void setPalette(List<String> palette) {
        this.palette = palette;
    }

    public static class ImageBean {
        private String url;
        /**
         * url : http://a.desktopprassets.com/wallpapers/47949f0c69ae431bc7526adcf526a62e74cce796/thumb_alienage.jpg
         * width : 296
         * height : 185
         */

        private ThumbBean thumb;
        /**
         * url : http://a.desktopprassets.com/wallpapers/47949f0c69ae431bc7526adcf526a62e74cce796/preview_alienage.jpg
         * width : 960
         * height : 600
         */

        private PreviewBean preview;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ThumbBean getThumb() {
            return thumb;
        }

        public void setThumb(ThumbBean thumb) {
            this.thumb = thumb;
        }

        public PreviewBean getPreview() {
            return preview;
        }

        public void setPreview(PreviewBean preview) {
            this.preview = preview;
        }

        public static class ThumbBean {
            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }

        public static class PreviewBean {
            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
