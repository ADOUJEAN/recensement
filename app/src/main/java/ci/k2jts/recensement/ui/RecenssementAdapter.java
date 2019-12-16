package ci.k2jts.recensement.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.object.Recessement;

/**
 * Created by ADOU JOHN on 15,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class RecenssementAdapter extends ArrayAdapter<Recessement> {

        private Activity context;
        List<Recessement> recessements;

        public RecenssementAdapter(Activity context, List<Recessement> Recessements) {
            super(context, R.layout.item_recencement, Recessements);
            this.context = context;
            this.recessements = Recessements;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.item_recencement, null, true);

            TextView textnom = (TextView) listViewItem.findViewById(R.id.txt_nom);
            TextView textprenom = (TextView) listViewItem.findViewById(R.id.txt_prenom);
            TextView textcontact = (TextView) listViewItem.findViewById(R.id.txt_contact);
            TextView textdatenaiss = (TextView) listViewItem.findViewById(R.id.txt_date_naiss);
            ImageView image =  listViewItem.findViewById(R.id.image);

            Recessement recessement = recessements.get(position);


            textnom.setText(recessement.getNom());
            textprenom.setText(recessement.getPrenom());
            textcontact.setText(recessement.getContact());
            textdatenaiss.setText(recessement.getDatenaissance());

            PicassoClient.downloadImage(context,recessement.getPhoto(),image);

            return listViewItem;
        }
    }

class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.mipmap.ic_launcher).into(img);
        }else
        {
            Picasso.with(c).load(R.mipmap.ic_launcher).into(img);
        }
    }

}