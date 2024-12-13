package com.example.userlocationlogin.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userlocationlogin.R;
import com.example.userlocationlogin.RoomDB.AppDatabase;
import com.example.userlocationlogin.Models.User;
import com.example.userlocationlogin.databinding.ActivityDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {


    private AppDatabase database;
    ActivityDashboardBinding binding;
    private String TAG = "DashboardActivity.java";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private String mCurrentPhotoPath;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = AppDatabase.getInstance(this);  // Make sure this is initialized
        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize LocationRequest and LocationCallback
        setupLocationRequest();
        setupLocationCallback();

        // Request Permissions and Start Location Updates
        requestPermissions();

        // SharedPrefManager.getInstance(DashboardActivity.this).logout();
        // Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        new GetUsersTask().execute();
        binding.trackIn.setOnClickListener(v -> {
            // Request permissions at runtime for Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }else {
                try {
                    openCamera();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // AsyncTask to perform database operation in the background
    private class GetUsersTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... voids) {
            try {
                // Access the database in the background
                return database.userDao().getAllUsers();
            } catch (Exception e) {
                // Log the error to Logcat
                Log.d(TAG, "Error fetching users from database", e);
                return null;  // Return null to indicate an error
            }
        }

        @Override
        protected void onPostExecute(List<User> users) {
            if (users != null) {
                StringBuilder sb = new StringBuilder();
                for (User user : users) {
                    sb.append("ID: ").append(user.getId())
                            .append(", Username: ").append(user.getUsername())
                            .append(", Email: ").append(user.getEmail())
                            .append(", Phone: ").append(user.getPhone())
                            .append(", Password: ").append(user.getPassword())
                            .append("\n");
                }
                Log.d(TAG, "onCreate: this show data === " + sb.toString());
            } else {
                // Handle the error case (e.g., show a toast)
                Log.d(TAG, "Error: Users data is null.");
                Toast.makeText(DashboardActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openCamera() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there's a camera activity available
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file where the image will be stored (using external storage)
            File photoFile = createImageFile(); // Method to create a file where the image will be stored
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.userlocationlogin.fileprovider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            // Start camera activity
            startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private File createImageFile() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the image URI from the camera intent
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

            // Set the image to the ImageView
            binding.imagePacker.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                try {
                    openCamera();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setupLocationRequest() {
        locationRequest = new LocationRequest.Builder(
                LocationRequest.PRIORITY_HIGH_ACCURACY, // High accuracy
                5000 // Interval: 5 seconds
        )
                .setMinUpdateIntervalMillis(2000) // Minimum interval: 2 seconds
                .setWaitForAccurateLocation(true) // Wait for more accurate location if possible
                .build();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (android.location.Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    float accuracy = location.getAccuracy();

                    Log.d("LocationUpdate", "Latitude: " + latitude + ", Longitude: " + longitude + ", Accuracy: " + accuracy);
                    Toast.makeText(DashboardActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude + ", Accuracy: " + accuracy, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void requestPermissions() {
        ActivityResultLauncher<String[]> permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.get(android.Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = result.get(android.Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (Boolean.TRUE.equals(fineLocationGranted) || Boolean.TRUE.equals(coarseLocationGranted)) {
                        startLocationUpdates();
                    } else {
                        Log.e("Location", "Permission Denied");
                    }
                });

        permissionLauncher.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void startLocationUpdates() {
        // Check if permissions are granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location", "Permissions not granted!");
            return;
        }

        // Request location updates
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper() // Pass the main looper to ensure updates happen on the main thread
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop location updates to prevent memory leaks
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}