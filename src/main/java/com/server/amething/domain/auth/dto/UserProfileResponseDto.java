package com.server.amething.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private long id;
    private String connected_at;
    private Properties properties;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class KakaoAcount {
        public Boolean profile_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public class Profile {
            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url;
            public boolean is_default_image;
        }
    }

}