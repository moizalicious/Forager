package diaspora.forager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Community extends AppCompatActivity {

    private static final String TAG = "Community";

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private TextView tribeName;
    private TextView distanceRemaining;

    private void setComponents() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        tribeName = (TextView) findViewById(R.id.tribeName);
        distanceRemaining = (TextView) findViewById(R.id.distanceRemaining);
    }

    private void setCompleteUI() {
        setDistanceRemainingToUI(databaseReference, firebaseAuth.getCurrentUser());
    }

    private void setDistanceRemainingToUI(final DatabaseReference databaseReference, FirebaseUser firebaseUser) {
        databaseReference.child("global")
                .child("distanceRemaining")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Update the android UI
                        setDistanceRemainingToUI(dataSnapshot.getValue(Long.class).intValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Database Error Occurred", databaseError.toException());
                        FirebaseCrash.report(databaseError.toException());
                    }
                });
    }

    private void setDistanceRemainingToUI(int distanceRemaining) {
        this.distanceRemaining.setText(Integer.toString(distanceRemaining));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        setComponents();
        setCompleteUI();
    }
}
