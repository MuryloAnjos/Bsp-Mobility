package com.bsp.bspmobility.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera setters, getters, toString, etc.
@NoArgsConstructor // Construtor vazio

public class Bus {
    private String LineCode;
    private double Latitude;
    private double Longitude;
    public String getLineCode() {
		return LineCode;
	}
	public void setLineCode(String lineCode) {
		LineCode = lineCode;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public boolean isHasPlatform() {
		return hasPlatform;
	}
	public void setHasPlatform(boolean hasPlatform) {
		this.hasPlatform = hasPlatform;
	}
	public int getEstimatedTimeArrival() {
		return estimatedTimeArrival;
	}
	public void setEstimatedTimeArrival(int estimatedTimeArrival) {
		this.estimatedTimeArrival = estimatedTimeArrival;
	}
	private boolean hasPlatform;
    private int estimatedTimeArrival;
}