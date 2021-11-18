package com.oslsoftware.clientapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MyCart extends Fragment {
    public final String logTag = "MyCartLog";
    String UserLocation;
ListView orderListView;
orderItemAdapter adapter;
ArrayList<orderItem> orderItems;
Button ConfirmOrderbt;
TextView txtnoOrders;
private Retrofit retrofit;
    LocationManager mLocationManager;
    Location loc;
Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    FirebaseUser user;
public  String bin2hex(byte[] data)
{

    StringBuilder hex = new StringBuilder(data.length * 2);
    for (byte b : data)
        hex.append(String.format("%02x", b & 0xFF));
    return hex.toString();

}


    public void setMyCartFragLocation()
    {


        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)// Which should  pass !!!!
        {


            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d(logTag,"Location has changed per request setting it ...");
                    loc = location;
                }
                @Override
                public void onProviderDisabled(String provider)
                {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.gps_disabled))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {


                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),20);
                                    dialog.dismiss();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();

                }
            });
loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

    }
    public String makeId(List<orderItem> items)
    {
        if(items.size() > 1)
        {
            //ArrayList<String> items = Arrays.asList("Pizza Viande hachée","Burger Ham");
            byte[] temp = items.get(0).getName().getBytes();
            for(int i = 1;i < items.size() ;i++ )
            {

                temp = makeXORbyteArray(items.get(i).getName().getBytes(),temp);

            }
            String res = bin2hex(temp);
            return res;
        }
        else
        {
            String s = items.get(0).getName() ;
            byte[] arr1 = s.substring(0, (int) Math.floor(s.length()/2)).getBytes();
            byte[] arr2 =  s.substring(s.length()/2 ,s.length()).getBytes();
            byte[] result = makeXORbyteArray(arr1,arr2);
            String res = bin2hex(result);
            return res;
        }

    }
    public byte[] makeXORbyteArray(byte[] arr1,byte[] arr2)
    {
        if(arr1 != null && arr2!= null)
        {
            if(arr1.length != arr2.length)
            {
                byte[] bigger = arr1.length > arr2.length ? arr1:arr2;
                byte[] smaller = arr1.length < arr2.length ? arr1:arr2;
                byte[] result = new byte[bigger.length];

                for(int i = 0 ;i <bigger.length;i++  )
                {
                    result[i] = (byte) (bigger[i] ^ smaller[i % smaller.length]);
                }

                return result;
            }
            else
            {
                byte[] result = new byte[arr1.length];
                for(int i = 0;i< arr1.length;i++)
                {
                    result[i] = (byte) (arr1[i] ^ arr2[i]);
                }
                return result;
            }
        }
        return null;
    }
public class Order
{
    //List<orderItem> orderItems;
//TODO: add notes for "hrissa,mayonnaise ,...etc"
    int totalValue;
    String location;
    String orderId;
    String uid;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId() {
        if(this.items != null)
        this.orderId = makeId(this.items);

    }

    int status;
    String restaurant;
    List <orderItem> items;

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public String getLocation() {
        return location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public List<orderItem> getItems() {
        return items;
    }

    public void setItems(List<orderItem> items) {
        this.items = items;
    }

    public Order(List<orderItem> items, int totalValue, int status)  {
        //this.orderItems = orderItems;
        this.totalValue = totalValue;
        this.items = items;
        this.status = status;
        this.location = "";
        this.restaurant = items.get(0).makingRestaurant.name;
        this.orderId = makeId(items);
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

    public MyCart()
    {
        // Required empty public constructor
    }
    public interface DeliverServiceAPI
    {
        @POST("/Orders")
        Call<Order> sendOrder(@Body Order order);
        @GET("get-Order-State/{restaurant}/{orderId}")
        Call<Integer> getOrderState(@Path("restaurant") String restaurant,@Path("orderId") String orderId);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new orderItemAdapter(getContext());
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
       user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderListView = view.findViewById(R.id.order_listView);
        ConfirmOrderbt = view.findViewById(R.id.bt_ConfOrder);
        txtnoOrders = view.findViewById(R.id.txtNoOrders);
        ViewGroup.LayoutParams params = orderListView.getLayoutParams();
        //TODO : get Device metrics and size the list view accordingly
    }
    public void setOrderItems(ArrayList<orderItem> orderItems) {
        this.orderItems = orderItems;
        Log.d("FragmentMyCart","order items set with size " + this.orderItems.size());
    }
public int totalPrice(ArrayList<orderItem> orderItemArrayList)
{
   int  result = 0;
   ArrayList<Integer> prices = new ArrayList<>();
    for(orderItem item:orderItemArrayList)
    {
        prices.add(item.getPrice() * item.getQuantity());
    }
    for(int a : prices)
    {
        result = result  + a;
    }
    return result;
}
public boolean checkSameRestaurant(ArrayList<orderItem> items)
    {
        String restaurant = items.get(0).getMakingRestaurant().getName();
        for(orderItem i : items)
        {
            if(!i.getMakingRestaurant().equals(restaurant))
            {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();

setMyCartFragLocation();
        if(loc == null)
            Log.d(logTag,"User Location not Available !");
        else
            UserLocation = loc.getLatitude() + "," + loc.getLongitude();

        ProgressDialog pWait = new ProgressDialog(getContext());
        pWait.setMessage(getString(R.string.sending_order));
        pWait.setCancelable(false);
        AlertDialog dialogSuccess = new AlertDialog.Builder(getContext()).create();
        AlertDialog dialogFaliure =  new AlertDialog.Builder(getContext()).create();
        dialogSuccess.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogSuccess.setMessage(getString(R.string.order_sent));
        dialogFaliure.setMessage(getString(R.string.sending_failed));
        Callback<Order> ordersCallback = new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                        /*try {
                            Thread.sleep(2000 );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                pWait.dismiss();
                dialogSuccess.show();
                Log.d(logTag,"Order Sent Successfully");
                //Log.d(logTag,call.request().body().toString());
                dialogSuccess.dismiss();
                Intent intent = new Intent(getActivity(),OrderDelivery.class);
                intent.putExtra("orderId",response.body().getOrderId());
                Log.d(logTag,"orderId is : " + response.body().getOrderId());
                intent.putExtra("UserLocation",UserLocation);
                Log.d(logTag,"user Location" + UserLocation);
                intent.putExtra("restaurant",response.body().restaurant);
                Log.d(logTag,"user Location" + UserLocation);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.d(logTag,"Failed to send Order " + t.getMessage());
                pWait.dismiss();
                dialogFaliure.show();
                Toast.makeText(getContext(), "Failed : " + t.toString(), Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(800 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialogFaliure.dismiss();
            }
        };
        adapter.setItems(orderItems);
        //Log.d("FragmentMyCart","order items et to adapter with size" + orderItems.size());
        orderListView.setAdapter(adapter);
        if(orderItems.isEmpty())
        {
            ConfirmOrderbt.setVisibility(View.GONE);
            txtnoOrders.setVisibility(View.VISIBLE);
        }
   if(!orderItems.isEmpty())
   {
       ProgressDialog progressDialog = new ProgressDialog(getContext());
       txtnoOrders.setVisibility(View.GONE);
       Order order = new Order(orderItems,totalPrice(orderItems),0 /*RECIEVED*/);
       order.setLocation(UserLocation);
       order.setTotalValue(totalPrice((ArrayList<orderItem>) order.getItems()));
       Log.d(logTag,"Object order has an id of : " + order.getOrderId());
       Log.d(logTag,"order has value of  : " + order.getTotalValue() );
        /*dialogFaliure.setButton(AlertDialog.BUTTON_NEGATIVE, "Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
               orders.enqueue(ordersCallback);
            }
        });*/
       ConfirmOrderbt.setOnClickListener(v -> {
           loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           progressDialog.setMessage(getString(R.string.wait_location));
           Log.d(logTag,"Sending Order ...");

           Dialog dialog = new Dialog(mContext);
           View parentDialogue = LayoutInflater.from(mContext).inflate(R.layout.confirm_order_layout,null,false);
           Button confirm = parentDialogue.findViewById(R.id.confirmOrderWithFeeBt);
           TextView orderFeetxt = parentDialogue.findViewById(R.id.txtOrderFee);
           TextView deliveryFeetxt = parentDialogue.findViewById(R.id.txtDeliveryFee);
           orderFeetxt.setText(order.getTotalValue() + " DZD");
           deliveryFeetxt.setText("120 DZD");
           dialog.setContentView(parentDialogue);
           confirm.setOnClickListener(v1 -> {
                   if(order.getLocation() == null)
                   {
                       getActivity().runOnUiThread(()->
                       {
                           progressDialog.show();
                       });
                       Thread th =  new Thread(() -> {
                           Log.d(logTag,"location thread started");
                           while(loc == null)
                           {
                               try {
                                   Thread.sleep(1000);
                                   Log.d(logTag,"loc is still null");
                                   loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                           }

                       }

                           UserLocation = loc.getLatitude() + "," + loc.getLongitude();
                           order.setLocation(UserLocation);
                           Log.d(logTag,"Location got enqueueing call ");
                           retrofit.create(DeliverServiceAPI.class).sendOrder(order).enqueue(ordersCallback);
                           getActivity().runOnUiThread(()->{
                               progressDialog.dismiss();
                               pWait.show();
                           });

                           Log.d(logTag,"Call enqueued");

                       });
                       th.start();
                   }
                   else
                   {
                       retrofit.create(DeliverServiceAPI.class).sendOrder(order).enqueue(ordersCallback);
                       pWait.show();
                       Log.d(logTag,"Call enqueued");
                   }
               dialog.dismiss();

               });
dialog.show();
          ;
           // if(checkSameRestaurant((ArrayList<orderItem>) order.items))
           // else
           //Toast.makeText(getContext(), "Items cannot be of different restaurants", Toast.LENGTH_SHORT).show();
       });
   }
   else
   {
       txtnoOrders.setVisibility(View.VISIBLE);
   }

    }
}