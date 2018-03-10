package com.example.android.loginlibrary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ritik on 10-03-2018.
 */

public class SimpleEmailLogin {
    public static FirebaseAuth mAuth;

    public SimpleEmailLogin() {

    }

    private OnEmailLoginResult mOnEmailLoginResult;

    public interface OnEmailLoginResult {
        public void resultSuccessful(FirebaseUser registeredUser);

        public void resultError(Exception errorResult);

        public void wrongCrudentials(String errorMessage);
    }

    public void setOnEmailLoginResult(OnEmailLoginResult eventListener) {
        mOnEmailLoginResult = eventListener;
    }

    public void attemptLogin(@NonNull Activity var1, String email, String passwordinput) {
        email = email.trim();
        if (!checkCrudentials(email, passwordinput).equals("valid")) {
            if (mOnEmailLoginResult != null) {
                mOnEmailLoginResult.wrongCrudentials(checkCrudentials(email, passwordinput));
                return;
            }
        }
        mAuth = FirebaseAuth.getInstance();

        Log.i("login attempted", "point 171");

        mAuth.signInWithEmailAndPassword(email, passwordinput)
                .addOnCompleteListener(var1, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("signInWithEmail:success", "point 187");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (mOnEmailLoginResult != null) {
                                mOnEmailLoginResult.resultSuccessful(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                if (mOnEmailLoginResult != null) {
                                    mOnEmailLoginResult.resultError(task.getException());
                                }
                            }

                        }
                    }
                });
    }


    private static String checkCrudentials(String email, String passwordinput) {
        if (!emailCheck(email)) {
            return "invalid email";
        }
        if (!passwordCheck(passwordinput)) {
            return "invalid passwordinput";
        }
        return "valid";
    }

    private static boolean passwordCheck(String password) {
        return TextUtils.isEmpty(password) || !(password.length() < 7);
    }

    private static boolean emailCheck(String email) {
        if (TextUtils.isEmpty(email)) {
            Log.i("point 506", "email null");
            return false;
        } else if (!email.contains("@")) {

            return false;
        } else if (!email.contains(".")) {

            return false;
        }
        return true;
    }

}
