package ci.k2jts.recensement.ui.object;

/**
 * Created by ADOU JOHN on 15,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class Recessement {
    public String id;
    public String nom;
    public String prenom;
    public String datenaissance;
    public String daterecensement;
    public String latitude;
    public String longitude;
    public String contact;
    public String photo;

    public Recessement() {
    }

    public Recessement(String id, String nom, String prenom, String datenaissance, String daterecensement, String latitude, String longitude, String contact, String photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.daterecensement = daterecensement;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contact = contact;
        this.photo = photo;
    }

    public Recessement(String id, String nom, String prenom, String datenaissance, String contact) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getDaterecensement() {
        return daterecensement;
    }

    public void setDaterecensement(String daterecensement) {
        this.daterecensement = daterecensement;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
