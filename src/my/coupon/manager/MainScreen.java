package my.coupon.manager;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends Activity {
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);

		// TextViews
		final TextView welcomeUserText = (TextView) findViewById(R.id.tWelcomeUser);

		// Buttons 
		//final Button scanACouponButton = (Button) findViewById(R.id.bScanCoupon);
		final Button logoutButton = (Button) findViewById(R.id.bLogout2);
		final Button couponListButton = (Button) findViewById(R.id.bCouponList);
		//final Button settingsButton = (Button) findViewById(R.id.bSettings);
		//final Button shoppingListButton = (Button) findViewById(R.id.bShoppingList);
		
		String usersName = null;

		// first change tWelcomeUser to welcome back + name
		ParseUser theUser = ParseUser.getCurrentUser();
		usersName = theUser.getUsername();
		welcomeUserText.setText("Welcome back " + usersName);
		
		
		logoutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// log user out
				ParseUser.logOut();
				//ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
				// go back to login screen
				Intent goToLoginScreen=new Intent(MainScreen.this, CouponManagerActivity.class);
				startActivity(goToLoginScreen);
			}
		});
		
		couponListButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent goToCouponList=new Intent(MainScreen.this, CouponList.class);
				startActivity(goToCouponList);
			}
		});
		
	}

}
