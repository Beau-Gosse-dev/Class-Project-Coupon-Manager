package my.coupon.manager;

import java.util.regex.Pattern;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends Activity {
	
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);


		//buttons 
		final Button submitButton = (Button) findViewById(R.id.bSignupSubmit);

		submitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// make sure they are connected to internets first
				if(haveInternet()){
				dialog = ProgressDialog.show(SignUp.this, "", 
                        "Please wait, signing up...", true);
				dialog.show();
				if(validateForm()){
					// form is good
					
				}
				else{
					dialog.dismiss();
				}
				}
				else{
					// no internet connection
					displayErrorPopup("Error: You need an internet connection to sign up");
				}
			}
		});

		// TODO Auto-generated method stub
	}

	private boolean validateForm(){

		final EditText tUsername = (EditText) findViewById(R.id.textUsername);
		final EditText tPassword = (EditText) findViewById(R.id.textPassword);
		final EditText tEmail = (EditText) findViewById(R.id.textEmail);

		String sName, sPassword, sEmail, errorMessage;
		// checks to make sure the username, password and email are set

		// check username
		// must be atleast 5 characters

		sName = tUsername.getText().toString();
		sPassword = tPassword.getText().toString();
		sEmail = tEmail.getText().toString();


		System.out.println(sName + " " + sPassword + " " + sEmail);

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

		if(sEmail == null || sEmail.length() == 0){
			errorMessage = "Error: Enter an email";
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
		// check for spaces in email field
		for(int i=0; i<sEmail.length(); i++){
			if(Character.isWhitespace(sEmail.charAt(i))){
				// no spaces allowed
				errorMessage = "Error: No spaces allowed in email";
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

		if(sEmail.length() < 7){
			errorMessage = "Error: Email address must be 7 characters or longer";
			displayErrorPopup(errorMessage);
			return false;	
		}

		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
				"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
						"\\@" +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
						"(" +
						"\\." +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
						")+"
				);

		if(!EMAIL_ADDRESS_PATTERN.matcher(sEmail).matches()){
			// invalid email address
			errorMessage = "Error: Email is not in the form user@email.com";
			displayErrorPopup(errorMessage);
			return false;	
		}

		// now try to register the user, returns true if successfull
		registerUser(sName, sPassword, sEmail);

		return true;
	}

	private void registerUser(String username, String password, String email){
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				
				if (e == null) {
					// Worked!
					loginUser();
					dialog.dismiss();
					displayCongrats("Thank you for signing up!");
					// goes back to main screen
					
				} else {
					String theProblem = null;
					if(e.getCode() == ParseException.EMAIL_TAKEN)
						theProblem = "Email already in use";
					if(e.getCode() == ParseException.CONNECTION_FAILED)
						theProblem = "Connection problem, please make sure your connected to the internet and try again later.";
					if(e.getCode() == ParseException.USERNAME_TAKEN)
						theProblem = "The username is already taken, please try another";
					// shouldn't get these b/c of form validation on our end
					if(e.getCode() == ParseException.EMAIL_MISSING)
						theProblem = "Problem with your email address, please re-enter it";
					if(e.getCode() == ParseException.PASSWORD_MISSING)
						theProblem = "Problem with your password, please re-enter it";
					if(e.getCode() == ParseException.USERNAME_MISSING)
						theProblem = "Problem with your username, please re-enter it";
					// display a popup with the correct error
					dialog.dismiss();
					if(theProblem != null){
						displayErrorPopup(theProblem);
					}
					else{
						displayErrorPopup("Problem signing up. Code: " + e.getCode());
					}
				}
			}
		});

	}


	private void displayCongrats(String theMessage){
		AlertDialog.Builder popupCongrats = new AlertDialog.Builder(this);
		popupCongrats.setMessage(theMessage)
		.setPositiveButton("Log me in!", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				// log user in and go to home screen
				dialog.cancel();
				dialog.dismiss();
				finishActivity();
			}

		});

		// create it
		popupCongrats.create();
		// now show it
		popupCongrats.show();

	}

	
	private void finishActivity(){
		this.finish();
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
	
	private void loginUser(){
		// auto gets the username and password from textboxes b/c user 
		//can't change them after signing up successfully
		

		final EditText tUsername = (EditText) findViewById(R.id.textUsername);
		final EditText tPassword = (EditText) findViewById(R.id.textPassword);
		
		String username = tUsername.getText().toString();
		String password = tPassword.getText().toString();
		ParseUser.logInInBackground(username, password, new LogInCallback() {
		    public void done(ParseUser user, ParseException e) {
		    	
		    	dialog.dismiss();
		        if (e == null && user != null) {
		            // go back to main screen
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

}
