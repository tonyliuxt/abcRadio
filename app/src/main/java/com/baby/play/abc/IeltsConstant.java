package com.baby.play.abc;

public class IeltsConstant {
	
	public static final String PREFIX = "IeltsSpeak";
	public static final String EXTENSION = ".amr";
	
	public static final String NET_HINT = "Network is not available!";
	public static final String URL_SWITCH = "Opps, can you click me once again?";
	
	public static final String adSpaceid = "F5kb5eIDar"; // abc radio
	public static final String adSpaceidChaPing = "uPFGPJxsuN"; // abc radio
	public static final boolean isTest = false;
	
	public static final String URL_PREFIX[] = {"http://live-radio02","http://live-radio01"};
	public static final String URL_MIDDLE[] = {".mediahubaustralia.com"};
	public static final String URL_APPEND[] = {"mp3/","aac/"};
	
	public static String DYNAMIC_PREFIX = "http://live-radio02";
	public static String DYNAMIC_MIDDLE = ".mediahubaustralia.com";
	public static String DYNAMIC_APPEND = "mp3/";
	
	public static final String A_COUNTRY 	= "/CTRW/";	//"http://shoutmedia.abc.net.au:10320/";//
	public static String getA_COUNTRY(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+A_COUNTRY+DYNAMIC_APPEND;
	}
	public static final String A_CLASSIC 	= "/2FMW/";	//"http://shoutmedia.abc.net.au:10322/";//
	public static String getA_CLASSIC(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+A_CLASSIC+DYNAMIC_APPEND;
	}
	public static final String A_GRANDSTAND = "/GSDW/";	//"http://shoutmedia.abc.net.au:10462/";//
	public static String getA_GRANDSTAND(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+A_GRANDSTAND+DYNAMIC_APPEND;
	}
	
	public static final String B_SYDNEY 	= "/2LRW/";	//"http://shoutmedia.abc.net.au:10336/";//
	public static String getB_SYDNEY(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+B_SYDNEY+DYNAMIC_APPEND;
	}
	public static final String B_MELBOURNE 	= "/3LRW/";	//"http://shoutmedia.abc.net.au:10450/";//
	public static String getB_MELBOURNE(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+B_MELBOURNE+DYNAMIC_APPEND;
	}
	public static final String B_ADELAIDE 	= "/5LRW/";	//"http://shoutmedia.abc.net.au:10352/";//
	public static String getB_ADELAIDE(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+B_ADELAIDE+DYNAMIC_APPEND;
	}

	public static final String C_CANBERRA 	= "/1LRW/";	//"http://shoutmedia.abc.net.au:10354/";//
	public static String getC_CANBERRA(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+C_CANBERRA+DYNAMIC_APPEND;
	}
	public static final String C_BRISBANE	= "/4LRW/";	//"http://shoutmedia.abc.net.au:10346/";//
	public static String getC_BRISBANE(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+C_BRISBANE+DYNAMIC_APPEND;
	}
	public static final String C_PERTH	 	= "/6LRW/";	//"http://shoutmedia.abc.net.au:10348/";//
	public static String getC_PERTH(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+C_PERTH+DYNAMIC_APPEND;
	}
	//-------------------------------------------------------------------------------------------------
	// New Add
	public static final String E_ABCJAZZ 	= "/JAZW/";// ABC Jazz
	public static String getE_ABCJAZZ(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+E_ABCJAZZ+DYNAMIC_APPEND;
	}
	public static final String E_CLASSICTWO	= "/FM2W/";//Classic Two
	public static String getE_CLASSICTWO(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+E_CLASSICTWO+DYNAMIC_APPEND;
	}
	public static final String E_DOUBLEJ	 	= "/DJDW/";//Double J
	public static String getE_DOUBLEJ(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+E_DOUBLEJ+DYNAMIC_APPEND;
	}

	public static final String F_2RNW 	= "/2RNW/";//Radio National
	public static String getF_2RNW(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+F_2RNW+DYNAMIC_APPEND;
	}
	public static final String F_PBW	= "/PBW/";//NationalNews
	public static String getF_PBW(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+F_PBW+DYNAMIC_APPEND;
	}
	public static final String F_2TJW	 	= "/2TJW/";// TripleJ
	public static String getF_2TJW(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+F_2TJW+DYNAMIC_APPEND;
	}

	public static final String H_IT1W	 	= "/IT1W/"; //itinerant one
	public static String getH_IT1W(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+H_IT1W+DYNAMIC_APPEND;
	}
	public static final String H_IT2W	 	= "/IT2W/";//itinerant two
	public static String getH_IT2W(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+H_IT2W+DYNAMIC_APPEND;
	}
	public static final String H_IT3W	 	= "/IT3W/";//itinerant three
	public static String getH_IT3W(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+H_IT3W+DYNAMIC_APPEND;
	}

	public static final String G_UNEARTHED  	= "/UNEW/";//Unearthed
	public static String getG_UNEARTHED(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+G_UNEARTHED+DYNAMIC_APPEND;
	}
	public static final String G_XTDW	= "/XTDW/"; // Extra
	public static String getG_XTDW(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+G_XTDW+DYNAMIC_APPEND;
	}
	//-------------------------------------------------------------------------------------------------
	public static String D_A = "/8LRW/";
	public static String D_AN = "Darwin"; 
	public static String getD_A(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+D_A+DYNAMIC_APPEND;
	}
	public static String D_B = "/7LRW/";	
	public static String D_BN = "Hobart";
	public static String getD_B(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+D_B+DYNAMIC_APPEND;
	}
	public static String D_C = "/4GLDW/"; 
	public static String D_CN = "GoldCoast";
	public static String getD_C(){
		return DYNAMIC_PREFIX+DYNAMIC_MIDDLE+D_C+DYNAMIC_APPEND;
	}

}
