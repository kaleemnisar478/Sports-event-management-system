package com.sports.sportseventmanagementsystem.Admin_Panel.Add_Players;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sports.sportseventmanagementsystem.R;
import com.sports.sportseventmanagementsystem.helperClasses.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AddPlayerDialog extends AppCompatDialogFragment {

    private TextInputLayout participantName,partyName;
    private ImageView profilePic;
    private FloatingActionButton profile;
    private Player player;
    private String state;

    private AlertDialog.Builder builder=null;
    private AlertDialog mAlertDialog=null;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    private ProgressDialog progressDialog;


    private Bitmap photoProfile;
    private Uri imageUriProfile;

    private String status;

    private String gameID,teamID;


    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_PICTURE = 1999;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_GALLERY_PERMISSION_CODE=200;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public AddPlayerDialog(Player player, String state, String gameID,String teamID) {
        this.player = player;
        this.state = state;
        this.gameID=gameID;
        this.teamID=teamID;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_players,null);

        progressDialog=new ProgressDialog(getActivity());
        status="profilePic";

        participantName=view.findViewById(R.id.dialog_participant_name);
        partyName=view.findViewById(R.id.partyName);

        profilePic=view.findViewById(R.id.profile_image);

        profile=view.findViewById(R.id.fabProfile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="profilePic";
                startDialog();
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("Games").child(gameID).child("Teams").child(teamID).child("Players");

        participantName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && participantName.getEditText().getText().toString().isEmpty())
                {
                    participantName.setError("Field cannot be empty");
                }
            }

        });

        partyName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && partyName.getEditText().getText().toString().isEmpty())
                {
                    partyName.setError("Field cannot be empty");
                }
            }

        });


        participantName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0) {
                    participantName.setError("Field cannot be empty");
                }
                else if(s.length()>0 && s.length()<3) {
                    participantName.setErrorEnabled(false);
                }
                else if (s.toString().trim().length()>=30){
                    participantName.setError("Maximum length reached");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        partyName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0) {
                    partyName.setError("Field cannot be empty");
                }
                else if(s.length()>0 && s.length()<3) {
                    partyName.setErrorEnabled(false);
                }
                else if (s.toString().trim().length()>=30){
                    partyName.setError("Maximum length reached");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if(state.equals("add")) {
            builder.setView(view)
                    .setTitle("Add New Player")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Add", null);
            mAlertDialog= builder.create();
            mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            mAlertDialog.show();


            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateName() | !validatePartyName()  | imageUriProfile==null) {
                        Toast.makeText(getContext(),"All fields are necessary", Toast.LENGTH_LONG).show();
                        return;
                    }
                    addParticipant();
                    mAlertDialog.dismiss();
                }
            });

        }
        else
        {
            participantName.getEditText().setText(player.getName());
            partyName.getEditText().setText(player.getEmail());

            profilePic.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            builder.setView(view)
                    .setTitle("Edit Player Info")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData();

                        }
                    }).setPositiveButton("Update", null)
                    .setNeutralButton("Cancel", null);

            mAlertDialog= builder.create();
            mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            mAlertDialog.show();



            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( !validateName() | !validatePartyName()) {
                        Toast.makeText(getContext(),"All fields are necessary", Toast.LENGTH_LONG).show();
                        return;
                    }
                    updateData();
                    mAlertDialog.dismiss();
                }
            });


        }


        return mAlertDialog;

    }


    private boolean validateName(){
        String val=participantName.getEditText().getText().toString().trim();
        final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            participantName.setError("Field cannot be empty");
            participantName.requestFocus();
            return false;
        }
        else if(!val.matches(name_checker))
        {
            participantName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePartyName(){
        String val=partyName.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            partyName.setError("Field cannot be empty");
            partyName.requestFocus();
            return false;
        }
        return true;
    }



    void addParticipant()
    {


        String name = participantName.getEditText().getText().toString();
        String pName = partyName.getEditText().getText().toString();

        final String id = databaseReference.push().getKey();


        player = new Player(id,name,"pic",pName);


        if(imageUriProfile!=null)
        {

            try {

                progressDialog.setMessage("Uploading Info ...");
                progressDialog.show();

                storageReference= FirebaseStorage.getInstance().getReference("Images").child("Game Images").child(gameID).child("Team Images").child(teamID);

                storageReference.child("Player Images").child(id).putFile(imageUriProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                player.setImage(uri.toString());
                                databaseReference.child(id).setValue(player);
                                progressDialog.dismiss();

                            }
                        });
                    }
                });


            } catch (Exception e) {

                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }



    }



    void updateData()
    {


        String name = participantName.getEditText().getText().toString();
        String pName = partyName.getEditText().getText().toString();
        Player newParticipant = new Player(player.getId(),name,player.getImage(),pName);

        databaseReference.setValue(newParticipant);

        Toast.makeText(getContext(),"Player Updated Successfully", Toast.LENGTH_LONG).show();

    }


    void deleteData()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference("Players").child(teamID);
        databaseReference.removeValue();
        Toast.makeText(getContext(),player.getName()+" Deleted Successfully", Toast.LENGTH_LONG).show();

    }



    private void startDialog() {
        android.app.AlertDialog.Builder myAlertDialog = new android.app.AlertDialog.Builder(getContext());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to upload your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_GALLERY_PERMISSION_CODE);

                        } else {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);


                        }

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }

        } else if(requestCode==MY_GALLERY_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Gallery read permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);
            } else {
                Toast.makeText(getContext(), "Gallery read permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Uri getImageUri(Activity inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            //imageUri = data.getData();
            if(status.equals("profilePic")) {
                 photoProfile= (Bitmap) data.getExtras().get("data");

                imageUriProfile = getImageUri(getActivity(), photoProfile);
                profilePic.setImageBitmap(photoProfile);
            }

        } else if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {


            // The following code snipet is used when Intent for single image selection is set
            if(status.equals("profilePic")) {

                imageUriProfile = data.getData();
                //Log.d(TAG,"file uri: "+imageUri.toString());
                try {
                    photoProfile = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUriProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                profilePic.setImageBitmap(photoProfile);

            }


        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Log.d(TAG, "PHOTO is null");
            getActivity().finish();
        }
    }


    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }


}
