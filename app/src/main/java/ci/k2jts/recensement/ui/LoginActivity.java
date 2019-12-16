package ci.k2jts.recensement.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ci.k2jts.recensement.ui.object.Inscription;
import ci.k2jts.recensement.MainActivity;
import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.utilities.Constants;
import ci.k2jts.recensement.ui.utilities.Util;

public class LoginActivity extends AppCompatActivity {


    String Login,Password;
    EditText edLogin,edPassword;
    Button btLogin, btInscription;

    ProgressBar progress;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = findViewById(R.id.progress);
        edLogin = findViewById(R.id.edt_login);
        edPassword = findViewById(R.id.edt_mot_de_passe);
        btLogin = findViewById(R.id.bt_login);
        btInscription = findViewById(R.id.bt_inscrire);

        progress.setVisibility(View.GONE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Util.checkPermissionAndroidApplication(this);



        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login = edLogin.getText().toString();
                Password = edPassword.getText().toString();

                if (Login.equalsIgnoreCase("") || Password.equalsIgnoreCase("")) {

                    Util.dialogWithAction(LoginActivity.this,
                            "Authentification",
                            "Veuillez saisir vos identifients !).",
                            "OK", R.mipmap.ic_launcher,
                            null);
                    return;
                }
                progress.setVisibility(View.VISIBLE);

                getListItems(Login,Password);
            }
        });

        btInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,InscriptionActivity.class));
            }
        });
    }





    private void getListItems(String login,String password) {
        firebaseFirestore.collection("inscription")
                .whereEqualTo("login", login).whereEqualTo("motdepasse",password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Constants.USER_ID = document.getId();

                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            progress.setVisibility(View.GONE);

                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                }

                ).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "onSuccess: LIST EMPTY");
                    progress.setVisibility(View.GONE);

                    Util.dialogWithAction(LoginActivity.this,
                            "Authentification",
                            "Veuillez vous assurer que vos parametres d'authentification sont correctes !",
                            "OK", R.mipmap.ic_launcher,
                            null);
                    return;
                } else {
                    progress.setVisibility(View.GONE);

                    List<Inscription> types = queryDocumentSnapshots.toObjects(Inscription.class);
                   Constants.mUSER.addAll(types);
                   startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);

                Util.dialogWithAction(LoginActivity.this,
                        "Authentification",
                        "Veuillez vous assurer que vos parametres d'authentification sont correctes !",
                        "OK", R.mipmap.ic_launcher,
                        null);
                return;

            }
        });

    }
}
