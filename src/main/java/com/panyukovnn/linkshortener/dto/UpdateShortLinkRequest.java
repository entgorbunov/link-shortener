package com.panyukovnn.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateShortLinkRequest {

	private UUID id;
	private String shortLink;
	private LocalDateTime endTime;
	private String description;
	private Boolean active;
}
