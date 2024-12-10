package com.panyukovnn.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShortLinkRequest {

	private String link;
	private LocalDateTime endTime;
	private String description;
	private Boolean active;

}
