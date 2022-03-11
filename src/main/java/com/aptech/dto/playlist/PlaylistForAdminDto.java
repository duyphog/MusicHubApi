package com.aptech.dto.playlist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Playlist;
import com.aptech.entity.PlaylistDetail;
import com.aptech.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistForAdminDto {
	private Long id;

	private String name;

	private String description;

	private String imageUrl;

	private CategoryDto category;

	private GenreDto genre;

	private PlaylistTypeDto playlistType;

	private Boolean isPublic;

	private Long liked;

	private Long listened;

	private List<PlaylistDetailDto> playlistDetails = new ArrayList<PlaylistDetailDto>();

	private Date dateNew;

	private String userNew;

	private Date dateEdit;

	private String userEdit;

	public static PlaylistForAdminDto CreateFromEntity(Playlist src) {
		PlaylistForAdminDto dest = new PlaylistForAdminDto();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.description = src.getDescription();

		if (src.getCategory() != null)
			dest.category = CategoryDto.CreateFromEntity(src.getCategory());

		if (src.getImageUrl() != null)
			dest.imageUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getImageUrl());

		if (src.getGenre() != null)
			dest.genre = GenreDto.CreateFromEntity(src.getGenre());

		if (src.getPlaylistType() != null)
			dest.playlistType = PlaylistTypeDto.CreateFromEntity(src.getPlaylistType());

		dest.isPublic = src.getIsPublic();
		dest.liked = src.getLiked();
		dest.listened = src.getListened();
		dest.dateNew = src.getDateNew();
		dest.dateEdit = src.getDateEdit();
		dest.userNew = src.getUserNew();
		dest.userEdit = src.getUserEdit();

		if (src.getPlaylistDetails() != null)
			for (PlaylistDetail detail : src.getPlaylistDetails()) {
				dest.playlistDetails.add(PlaylistDetailDto.CreateFromEntity(detail));
			}

		return dest;
	}
}
