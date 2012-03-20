package my.coupon.manager;

public class Coupon {
	
	private String couponName = "CouponNameNotSet";
	private String couponValue = "CouponVauleNotSet";
	
	public Coupon(String name, String value)
	{
		couponName = name;
		couponValue = value;
	}
	
	public String getName(){
		return couponName;
	}
	public String setName(String name){
		couponName = name;
		return couponName;
	}
	
	public String getValue(){
		return couponValue;
	}
	public String setValue(String value){
		couponValue = value;
		return couponValue;
	}

}
