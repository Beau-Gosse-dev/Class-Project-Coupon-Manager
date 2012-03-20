package my.coupon.manager;

import java.util.ArrayList;
import java.util.Iterator;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CouponList extends Activity {
	
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.couponlist);

		ArrayList<Coupon> listOfCoupons = new ArrayList<Coupon>();
		listOfCoupons.add(new Coupon("FirstTestCoupon", "Half Off"));
		listOfCoupons.add(new Coupon("SecondTestCoupon", "70% Off"));
		listOfCoupons.add(new Coupon("ThirdTestCoupon", "Buy One Get One Free"));

		Iterator<Coupon> iter = listOfCoupons.iterator();
		
		TableLayout tl = (TableLayout)findViewById(R.id.bCoupon);
		int rowNum = 0;
		
		
		
		
		while(iter.hasNext())
		{
			Coupon itemI = iter.next();

			TableRow tr = new TableRow(this);
 
			Button b = new Button(this);
			final String name = itemI.getName();
			final String value = itemI.getValue();
					
			b.setText(name);
			
			//b.setTag(itemI.getName());
			//b.setTag(2, itemI.getValue());
			
			b.setId(rowNum);
			b.setGravity(-1);
			rowNum++;			

			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				
					Intent intent=new Intent(CouponList.this, CouponDetails.class);

					intent.putExtra("string_Name",name);
					intent.putExtra("string_Value",value);
					startActivity(intent);
				}
			});
			
			tr.addView(b);
			tl.addView(tr);
		}
		
	}
}
