package my.coupon.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CouponDetails extends Activity {
	
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupondescription);
	
		// TextViews
		final TextView nameText = (TextView) findViewById(R.id.nameText);
		final TextView valueText = (TextView) findViewById(R.id.valueText);
		
		Intent i = getIntent();
		String name = i.getStringExtra("string_Name");
		String value = i.getStringExtra("string_Value");
		
		nameText.setText("Coupon Name: " + name);
		valueText.setText("The Value of this Coupon is: " + value);
	      
	}
}
