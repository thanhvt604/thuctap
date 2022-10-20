package com.globits.da;

public class Constants {
	public final static String dateTimeFormat="dd-MM-yyyy";
	public final static String 	REGEX_DATE="\\d{4}-((1[0-2])|(0[1-9]))-((0[1-9])|([1-2][0-9])|(3[0-1]))$";
	public final static String REGEX_CODE="^[\\w-!@#$%^&*]{6,10}$";
	public final static String REGEX_PHONE="^[\\d]{11}$";
	public final static String REGEX_EMAIL="^[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}$";
	public static final int MAX_SAME_TYPE_CERTIFICATE = 3;
	public static final int MIN_AGE = 0;
	public static final int NOT_HAS=0;


	public static enum StaffType {
		Sale(1), // nhân viên bán hàng
		Cashier(2), // nhân viên thu ngân
		Other(3)// khác
		;

		private Integer value;

		private StaffType(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	public static enum ChannelAds {// kenh quang cao
		Webiste(1), // website
		Contextual_Advertiser(2), // khen hquang cao
		Social_Netword(3), // mang xa hoi
		Youtube_channel(4)// youtube
		;

		private Integer value;

		private ChannelAds(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	public static enum Social_Netword {// kenh quang cao
		Facebook(1), // website
		Zalo(2), // khen hquang cao
		Tiktok(3), // mang xa hoi
		Other(4)// youtube
		;

		private Integer value;

		private Social_Netword(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

}
