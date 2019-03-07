package staclogintest.kr.hs.mirim.alone_test;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import staclogintest.kr.hs.mirim.alone_test.var.varStructure;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public static varStructure var = new varStructure();
    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 1000;

    // Firebase 인스턴스 변수
    private FirebaseAuth mFirebaseAuth;

    // GoogleApiClient 인스턴스 변수
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // FirebaseAuth 초기화
        mFirebaseAuth = FirebaseAuth.getInstance();

        // GoogleApiClient 초기화
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }//onCreate

    @Override
    public void onClick(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }//onClick

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }//onConnectionFailed

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 로그인 결과
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // 구글 로그인에 성공하면 파이어베이스에 인증
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // 구글 로그인 실패
                Log.e(TAG, "Google Sign-In failed.");
            }//else
        }//if(requestCode == RC_SIGN_IN)
    }//onActivityResult

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 인증에 성공하면 MainActivity 로 이동, 실패하면 에러 메시지 표시
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "인증성공", task.getException());
                            Toast.makeText(SignInActivity.this, "인증실패",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            var.UserID = FirebaseAuth.getInstance().getUid();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }//else
                    }//onComplete
                });//mFirebaseAuth
    }//firebaseAuthWithGoogle
}//SignInActivity

