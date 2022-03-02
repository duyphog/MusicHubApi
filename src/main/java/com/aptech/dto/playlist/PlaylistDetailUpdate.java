package com.aptech.dto.playlist;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDetailUpdate {

	@NotNull
	private Long playlistId;

	@NotNull
	private Long trackId;
	
	@NotNull
	private Boolean isRemove;
}
