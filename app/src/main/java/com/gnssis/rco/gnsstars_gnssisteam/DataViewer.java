/*
 * Copyright 2018 TFI Systems

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.gnssis.rco.gnsstars_gnssisteam;

import android.location.Location;

import java.util.Observable;

/**
 * Created by Mateusz Krainski on 25/03/2018 (The Galfins Team).
 *
 * Source code: https://github.com/TheGalfins/GNSS_Compare
 *
 * This class is defining an interface for date viewers
 */

public interface DataViewer {

    void onLocationFromGoogleServicesResult(Location location);
}