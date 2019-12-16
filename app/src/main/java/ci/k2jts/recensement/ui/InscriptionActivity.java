package ci.k2jts.recensement.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.utilities.Util;
import io.opencensus.internal.Utils;

public class InscriptionActivity extends AppCompatActivity {

    String nom, prenom,login,motDePasse;
    EditText edNom, edPrenom,edLogin,edMotDePasse;
    Button btSave;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        edNom= findViewById(R.id.edt_nom);
        edPrenom= findViewById(R.id.edt_prenom);
        edLogin= findViewById(R.id.edt_login);
        edMotDePasse= findViewById(R.id.edt_mot_de_passe);
        btSave= findViewById(R.id.btSaveRec);


        firebaseFirestore = FirebaseFirestore.getInstance();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = edLogin.getText().toString();
                motDePasse = edMotDePasse.getText().toString();
                nom = edNom.getText().toString();
                prenom = edPrenom.getText().toString();



                if (login.equalsIgnoreCase("") || motDePasse.equalsIgnoreCase("")
                        || nom.equalsIgnoreCase("") || prenom.equalsIgnoreCase("")) {
                    //
                    Util.dialogWithAction(InscriptionActivity.this,
                            "Inscription",
                            "Veuillez saisir tous les champs !",
                            "OK", R.mipmap.ic_launcher,
                            null);

                    return;
                }

                Map<String,String> inscMap =new HashMap<>();
                inscMap.put("nom",nom);
                inscMap.put("prenom",prenom);
                inscMap.put("login",login);
                inscMap.put("motdepasse",motDePasse);

                firebaseFirestore.collection("inscription").add(inscMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Util.dialogWithAction(InscriptionActivity.this,
                                "Inscription",
                                "Inscription effectuée!",
                                "OK", R.mipmap.ic_launcher,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                        Toast.makeText(InscriptionActivity.this, "Inscription effectuée!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error =e.getMessage();
                        Toast.makeText(InscriptionActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    private void fermer() {
        new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }.run();
    }
}
