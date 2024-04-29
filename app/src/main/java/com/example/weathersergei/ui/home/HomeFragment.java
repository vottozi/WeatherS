package com.example.weathersergei.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathersergei.databinding.FragmentHomeBinding;
import com.example.weathersergei.models.Clouds;
import com.example.weathersergei.models.Main;
import com.example.weathersergei.models.Model;
import com.example.weathersergei.models.Sys;
import com.example.weathersergei.models.Wind;
import com.example.weathersergei.remote_data.RetrofitBuilder;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Integer temperature;
    Integer tempMaximal;
    Integer tempMinimal;

    String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

    final String apiKey = " ";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.localtime.setText(currentTime);

        Call<Model> call = RetrofitBuilder.getInstance().getCurrentWeather("Bishkek", apiKey);

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Main main_model = response.body().getMain_model();
                    Wind wind_model = response.body().getWind_model();
                    Clouds clouds_model = response.body().getClouds_model();
                    Sys sys_model = response.body().getSys_model();

                    Double temp = main_model.getTemp();
                    Double tempMax = main_model.getTempMax();
                    Double tempMin = main_model.getTempMin();

                    temperature = makeFromFaringate(temp);
                    tempMaximal = makeFromFaringate(tempMax);
                    tempMinimal = makeFromFaringate(tempMin);
                    setCondition();

                    binding.tempC.setText(String.valueOf(temperature) + "°C");
                    binding.maxMinTemp.setText(String.valueOf(tempMaximal) + " °C↑ \n" + String.valueOf(tempMinimal) + " °C↓");

                    binding.cityName.setText("Bishkek");

                    binding.humidity.setText(main_model.getHumidity() + " %");
                    binding.pressure.setText(main_model.getPressure() + "\nmBar");

                    binding.wind.setText(wind_model.getSpeed() + " m/s");
                    binding.cloud.setText(clouds_model.getAll() + " %");

                    binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                    binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable throwable) {
                Toast.makeText(requireActivity(), "No data" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.slideUpBottomSheet.setOnClickListener(v -> {
            if (binding.bottomSheet.getVisibility() == View.GONE) {
                binding.bottomSheet.setVisibility(View.VISIBLE);
            } else {
                binding.bottomSheet.setVisibility(View.GONE);
            }

            binding.blueSky.setVisibility(View.VISIBLE);
            binding.sun.setVisibility(View.VISIBLE);
            binding.badWeatherSky.setVisibility(View.INVISIBLE);
            binding.inputCity.setText("");
            binding.condition.setText("...");
        });

        binding.search.setOnClickListener(v1 -> {
            if (!binding.inputCity.getText().toString().isEmpty()) {
                Call<Model> call = RetrofitBuilder.getInstance().getCurrentWeather(binding.inputCity.getText().toString(), apiKey);

                call.enqueue(new Callback<Model>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Model> call, @NonNull Response<Model> response) {
                        Main main_model = response.body().getMain_model();
                        Wind wind_model = response.body().getWind_model();
                        Clouds clouds_model = response.body().getClouds_model();
                        Sys sys_model = response.body().getSys_model();

                        Double temp = main_model.getTemp();
                        Double tempMax = main_model.getTempMax();
                        Double tempMin = main_model.getTempMin();

                        temperature = makeFromFaringate(temp);
                        tempMaximal = makeFromFaringate(tempMax);
                        tempMinimal = makeFromFaringate(tempMin);
                        setCondition();

                        binding.tempC.setText(String.valueOf(temperature) + "°C");
                        binding.maxMinTemp.setText(String.valueOf(tempMaximal) + " °C↑ \n" + String.valueOf(tempMinimal) + " °C↓");

                        binding.cityName.setText(binding.inputCity.getText().toString());

                        binding.humidity.setText(main_model.getHumidity() + " %");
                        binding.pressure.setText(main_model.getPressure() + "\nmBar");

                        binding.wind.setText(wind_model.getSpeed() + " m/s");
                        binding.cloud.setText(clouds_model.getAll() + " %");

                        binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                        binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));

                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable throwable) {
                        Toast.makeText(requireActivity(), "No data" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public int makeFromFaringate(double tt) {
        Integer gr = (int)(tt-273.15);
        return gr;
    }

    public void setCondition() {
        if (temperature > 20) {
            binding.condition.setText("hotter");
        }
        if (temperature <= 20 && temperature > 14) {
            binding.condition.setText("light \nsunny");
        } else {
            if (temperature > 12 && temperature <= 14) {
                setNoHotWeather();
                binding.condition.setText("cloudy");
            } else {
                if (temperature > 7 && temperature <= 12) {
                    setNoHotWeather();
                    binding.condition.setText("cold");
                } else {
                    if (temperature <= 7) {
                        setNoHotWeather();
                        binding.condition.setText("very \ncold");
                    }
                }
            }
        }
    }

    public void setNoHotWeather() {
        binding.blueSky.setVisibility(View.INVISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.VISIBLE);
    }

    public String getCurrDateTime(long m) {
        String new_m = java.text.DateFormat.getDateTimeInstance().format(new Date(m));
        return new_m;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}