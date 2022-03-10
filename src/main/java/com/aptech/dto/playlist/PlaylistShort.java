package com.aptech.dto.playlist;

import com.aptech.entity.Category;
import com.aptech.entity.Playlist;
import com.aptech.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistShort {
	private Long id;

	private String name;

	private String imageUrl;

	private Category category;

	private Long liked;

	private Long listened;

	public static PlaylistShort CreateFromEntity(Playlist src) {
		PlaylistShort dest = new PlaylistShort();

		dest.id = src.getId();
		dest.name = src.getName();

		if (src.getImageUrl() != null)
			dest.imageUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getImageUrl());

		dest.liked = src.getLiked();
		dest.listened = src.getListened();

		return dest;
	}
}
