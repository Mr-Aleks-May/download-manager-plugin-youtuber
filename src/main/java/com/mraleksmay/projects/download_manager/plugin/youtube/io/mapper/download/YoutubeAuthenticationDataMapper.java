package com.mraleksmay.projects.download_manager.plugin.youtube.io.mapper.download;

import com.mraleksmay.projects.download_manager.plugin.io.dto.download.AuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.AuthenticationDataMapper;
import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.youtube.io.dto.download.YoutubeAuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.download.Base64AuthenticationData;

public class YoutubeAuthenticationDataMapper implements AuthenticationDataMapper {
    @Override
    public AuthenticationDataDto toDto(AuthenticationData authData) {
        if (authData == null)
            return null;

        return new YoutubeAuthenticationDataDto()
                .setLogin(authData.getLogin())
                .setPassword(authData.getPassword());
    }

    @Override
    public AuthenticationData fromDto(AuthenticationDataDto authDataDto) {
        if (authDataDto == null)
            return null;

        return new Base64AuthenticationData(authDataDto.getLogin(), authDataDto.getPassword())
                .setLogin(authDataDto.getLogin())
                .setPassword(authDataDto.getPassword());
    }
}
