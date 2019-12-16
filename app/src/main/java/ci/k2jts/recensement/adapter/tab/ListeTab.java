package ci.k2jts.recensement.adapter.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.RecenssementAdapter;
import ci.k2jts.recensement.ui.object.Recessement;
import ci.k2jts.recensement.ui.utilities.Constants;
import ci.k2jts.recensement.ui.utilities.Util;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class ListeTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_liste, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Constants.PROGRESS.setVisibility(View.VISIBLE);
        listViewRecessements =  rootView.findViewById(R.id.listViewTracks);

        listViewRecessements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Recessement recessement = recessements.get(i);

                showUpdateDeleteDialog(recessement);

//                Toast.makeText(getActivity(), ""+recessement.datenaissance, Toast.LENGTH_SHORT).show();

            }
        });
        recessements=new ArrayList<>();
        return rootView;
    }

    private FirebaseFirestore firebaseFirestore;

    List< Recessement> recessements;
    ListView listViewRecessements;


    private boolean deleteRecenssement(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
        DocumentReference documentReference = firebaseFirestore.collection("population").document(id);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(),"Document is delete",Toast.LENGTH_LONG).show();
                getData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("population", e.getMessage());
            }
        });;

        return true;
    }

    private boolean updateRecenssement(String id, String name, String prenom, String dateNaiss,String contact) {
        DocumentReference documentReference = firebaseFirestore.collection("population").document(id);
        documentReference.update("nom", name);
        documentReference.update("prenom", prenom);
        documentReference.update("datenaissance", dateNaiss);
        documentReference.update("contact", contact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Document Updated",Toast.LENGTH_LONG).show();
                        getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("population", e.getMessage());
                    }
                });


        return true;
    }

    private void showUpdateDeleteDialog(final Recessement  recessement) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editNom = (EditText) dialogView.findViewById(R.id.edt_nom);
        final EditText editPrenom = (EditText) dialogView.findViewById(R.id.edt_prenom);
        final EditText editDateNaiss = (EditText) dialogView.findViewById(R.id.edt_date_nais);
        final EditText editContact = (EditText) dialogView.findViewById(R.id.edt_contact);

        editNom.setText(""+recessement.nom);
        editPrenom.setText(""+recessement.prenom);
        editDateNaiss.setText(""+recessement.datenaissance);
        editContact.setText(""+recessement.contact);


        final Button buttonUpdate =  dialogView.findViewById(R.id.bt_update);
        final Button buttonDelete =  dialogView.findViewById(R.id.bt_delete);

        dialogBuilder.setTitle(recessement.nom+" "+recessement.prenom);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editNom.getText().toString().trim();
                String prenom = editPrenom.getText().toString().trim();
                String dateNais = editDateNaiss.getText().toString().trim();
                String contact = editContact.getText().toString().trim();
                Constants.PROGRESS.setVisibility(View.VISIBLE);

                    updateRecenssement(recessement.id, name, prenom,dateNais,contact);
                    b.dismiss();

            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.PROGRESS.setVisibility(View.VISIBLE);
                deleteRecenssement(recessement.id);
                b.dismiss();


            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Constants.PROGRESS.setVisibility(View.VISIBLE);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
       Constants.PROGRESS.setVisibility(View.VISIBLE);
        getData();
    }



    public void getData(){
        recessements=new ArrayList<>();
        firebaseFirestore.collection("population")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                      // Constants.USER_ID = document.getId();

                                                       Log.d("TAG", document.getId() + " => " + document.getData());
                                                   }
                                               } else {

                                                   Log.d("TAG", "Error getting documents: ", task.getException());
                                               }
                                           }
                                       }

                ).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "onSuccess: LIST EMPTY");
                    Constants.PROGRESS.setVisibility(View.GONE);

                    Util.dialogWithAction(getActivity(),
                            "Liste",
                            "Aucun recenssment effectué!",
                            "OK", R.mipmap.ic_launcher,
                            null);
                    return;
                } else {

               int totalDoc= queryDocumentSnapshots.getDocuments().size();
               List<String> allId=new ArrayList<>();
               for (int i=0;i<totalDoc;i++){
                   allId.add(queryDocumentSnapshots.getDocuments().get(i).getId());
               }

                   String id= queryDocumentSnapshots.getDocuments().get(1).getId();

                    List<Recessement> data = queryDocumentSnapshots.toObjects(Recessement.class);
//                    Constants.mUSER.addAll(types);
//                    startActivity(new Intent(getActivity(), MainActivity.class));
                    recessements=new ArrayList<>();
                    for (int i=0;i<data.size();i++){
                        Recessement recessement=new Recessement(allId.get(i),
                                data.get(i).nom,data.get(i).prenom,data.get(i).getDatenaissance(),
                                data.get(i).daterecensement,data.get(i).latitude,data.get(i).longitude,
                                data.get(i).contact,data.get(i).photo);

                        recessements.add(recessement);
                    }

                    //creating adapter
                    RecenssementAdapter recessementAdapter = new RecenssementAdapter(getActivity(), recessements);
                    //attaching adapter to the listview
                    listViewRecessements.setAdapter(recessementAdapter);

                    Constants.PROGRESS.setVisibility(View.GONE);


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constants.PROGRESS.setVisibility(View.GONE);

                Util.dialogWithAction(getActivity(),
                        "Liste recenssement",
                        "Veuillez verifier votre connectivité!",
                        "OK", R.mipmap.ic_launcher,
                        null);
                return;

            }
        });

    }
}
