package my.coupon.manager;
import my.coupon.manager.R;
import com.parse.*;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CouponManagerActivity extends Activity {
	
	ProgressDialog dialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "qYRB1AeL6w4bBhqUNL5PXkjvlP3a6p4I3cO7AzoV",
				"IuO3VhamNuVikXzqk6OGXXPJi0nhzUxx6Iv4Qh26"); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//buttons 
		final Button signupButton = (Button) findViewById(R.id.bSignup);
		final Button loginButton = (Button) findViewById(R.id.bLogin);


		signupButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent=new Intent(CouponManagerActivity.this, SignUp.class);

				startActivityForResult(intent, 0);
			}
		});
		

		
		loginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// login code
				// make sure they are connected to internets first
				if(haveInternet()){
				dialog = ProgressDialog.show(CouponManagerActivity.this, "", 
                        "Please wait, logging in...", true);
				dialog.show();
				if(validateForm()){
					// form is good
					// login and go to next activity
				}
				else{
					dialog.dismiss();
				}
				}
				else{
					// no internet connection
					displayErrorPopup("Error: You need an internet connection to login in");
				}
			}
		
	});
	}
	/*
	* @return boolean return true if the application can access the internet
	*/
	private boolean haveInternet(){
	        NetworkInfo info = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	        if (info==null || !info.isConnected()) {
	                return false;
	        }
	        if (info.isRoaming()) {
	                // here is the roaming option you can change it if you want to disable internet while roaming, just return false
	                return true;
	        }
	        return true;
	}
	
	private boolean validateForm(){

		final EditText tUsername = (EditText) findViewById(R.id.textLoginUsername);
		final EditText tPassword = (EditText) findViewById(R.id.textLoginPassword);

		String sName, sPassword, errorMessage;
		// checks to make sure the username, password are set

		// check username
		// must be atleast 5 characters

		sName = tUsername.getText().toString();
		sPassword = tPassword.getText().toString();


		if(sName == null || sName.length() == 0){
			errorMessage = "Error: Enter a username";
			displayErrorPopup(errorMessage);
			return false;	
		}

		if(sPassword == null || sPassword.length() == 0){
			errorMessage = "Error: Enter a password";
			displayErrorPopup(errorMessage);
			return false;	
		}


		// check for spaces in username field
		for(int i=0; i<sName.length(); i++){
			if(Character.isWhitespace(sName.charAt(i))){
				// no spaces allowed
				errorMessage = "Error: No spaces allowed in username";
				displayErrorPopup(errorMessage);
				return false;
			}
		}
		// check for spaces in password field
		for(int i=0; i<sPassword.length(); i++){
			if(Character.isWhitespace(sPassword.charAt(i))){
				// no spaces allowed
				errorMessage = "Error: No spaces allowed in password";
				displayErrorPopup(errorMessage);
				return false;
			}
		}
		

		if(sName.length() < 5){
			errorMessage = "Error: Username must be 5 characters or longer";
			displayErrorPopup(errorMessage);
			return false;	
		}

		if(sPassword.length() < 4){
			errorMessage = "Error: Password must be 4 characters or longer";
			displayErrorPopup(errorMessage);
			return false;	
		}
		
		// login them in
		loginUser(sName, sPassword);
		return true;
	}
	

	private void loginUser(String username, String password){
		ParseUser.logInInBackground(username, password, new LogInCallback() {
		    public void done(ParseUser user, ParseException e) {
		    	dialog.dismiss();
		        if (e == null && user != null) {
		            // Hooray! The user is logged in.
		        	isUserLoggedIn();
		        } else if (user == null) {
		            // Sign up didn't succeed. The username or password was invalid
		        	displayErrorPopup("Incorrect username or password");
		        } else {
		        	String theProblem = null;
		            // There was an error. Look at the ParseException to see what happened.
		        	if(e.getCode() == ParseException.CONNECTION_FAILED)
						theProblem = "Connection problem, please make sure your connected to the internet and try again later.";
		        	displayErrorPopup(theProblem);
		        }
		        	
		    }
		});
	}
	
	private void isUserLoggedIn(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		    // go to main app screen
			Intent goToMainScreen=new Intent(CouponManagerActivity.this, MainScreen.class);
			startActivity(goToMainScreen);
		} else {
		    // not logged in allow to sign up or login
		}
	}
	
	private void displayErrorPopup(String theError){
		AlertDialog.Builder errorPopup = new AlertDialog.Builder(this);
		errorPopup.setMessage(theError)
		.setPositiveButton("Try Again", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();				
			}

		});

		// create it
		errorPopup.create();
		// now show it
		errorPopup.show();

	}
	
	@Override
	protected void onStart(){
		super.onStart();
		// check if user is logged in already
		isUserLoggedIn();
	}
}