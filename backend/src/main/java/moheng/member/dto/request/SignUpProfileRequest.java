package moheng.member.dto.request;

import moheng.member.domain.GenderType;

import java.time.LocalDate;

public class SignUpProfileRequest {
    private final String nickname;
    private final LocalDate birthday;
    private final GenderType genderType;
    private final String profileImageUrl;

    public SignUpProfileRequest(String nickname, LocalDate birthday, GenderType genderType, String profileImageUrl) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.genderType = genderType;
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}