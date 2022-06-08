package com.server.amething.domain.user.service;

import com.server.amething.domain.auth.dto.UserProfileResponseDto;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.dto.ProfileDto;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * user가 로그인 할때 실행되는 메소드.
     * 받아온 kakao oauth의 user 메타데이터중 oauthId를 가져와 해당 oauthId로 user를 가져온다
     * 그리고 해당 user의 정보들을 기반으로 token들을 생성한다.
     * user가 새로 로그인 하는 user일 경우 saveUser 메소드를 실행하여 모든 정보들을 저장
     * 만약 기존에 로그인한 이력이 있는 user라면 user의 정보중 kakao oauth에서 받아오는 데이터인 profilePicture와 username만 update
     * @param userProfileResponseDto user의 kakao oauth 메타데이터
     * @param roles user의 role
     * @return Map<String, String> (key = accessToken, refreshToken)
     * @author 김태민
     */
    @Override
    public Map<String, String> login(UserProfileResponseDto userProfileResponseDto, List<Role> roles) {
        Optional<User> user = userRepository.findByOauthId(userProfileResponseDto.getId());
        Map<String, String> tokens = createToken(String.valueOf(userProfileResponseDto.getId()), roles);

        if (user.isPresent()) updateUser(user.get(), userProfileResponseDto);
        else saveUser(userProfileResponseDto, tokens.get("refreshToken"));

        return tokens;
    }

    /**
     * user의 oauthId를 기반으로 token을 생성하는 메소드
     * @param oauthId oauthId
     * @param roles user의 role
     * @return Map<String, String> (key = accessToken, refreshToken)
     */
    private Map<String, String> createToken(String oauthId, List<Role> roles) {
        String accessToken = jwtTokenProvider.createAccessToken(oauthId, roles);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        Map map = new HashMap();

        map.put("accessToken", "Bearer " + accessToken);
        map.put("refreshToken", "Bearer " + refreshToken);

        return map;
    }

    /**
     * 새로 로그인하는 user일 경우 user의 정보를 새로 저장해주는 메소드
     * @param userProfileResponseDto user의 kakao oauth 메타데이터
     * @param refreshToken refreshToken
     * @author 김태민
     */
    private void saveUser(UserProfileResponseDto userProfileResponseDto, String refreshToken) {
        userRepository.save(User.builder()
                .oauthId(userProfileResponseDto.getId())
                .nickname(userProfileResponseDto.getProperties().getNickname())
                .profilePicture(userProfileResponseDto.getProperties().getProfile_image())
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .bio("")
                .refreshToken(refreshToken)
                .build()
        );
    }

    /**
     * 기존에 로그인 했었던 user일 경우 Profile Picture와 Username만 update 해주는 메소드
     * @param user 로그인 할 user
     * @param userProfileResponseDto user의 kakao oauth 메타데이터
     * @author 김태민
     */
    private void updateUser(User user, UserProfileResponseDto userProfileResponseDto) {
        user.changeProfilePicture(userProfileResponseDto.getProperties().getProfile_image());
        user.changeNickname(userProfileResponseDto.getProperties().getNickname());
    }

    @Override
    public ProfileDto loadProfile(String nickname) {
        ProfileDto profileDto = userRepository.findProfileByNickname(nickname);
        return profileDto;
    }
}
