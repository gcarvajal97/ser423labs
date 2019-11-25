package edu.asu.bsse.gcarvaj3.ser423labapp;

/**
 * Copyright 2019 Gianni Carvajal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Gianni Carvajal mailto:gcarvaj3@asu.edu
 * @version November 25, 2019
 */

class Constants {
    static final int REQUEST_FOR_UPDATE = 0;
    static final int REQUEST_FOR_DELETE = 1;
    static final int REQUEST_FOR_ADDITION = 2;
    static final int KNOTS_PER_DEGREE = 60;
    static final double KNOTS_TO_MILES_CONVERSION = 1.15078;
    static final String INVALID_NUMBER_FORMAT_MESSAGE =
            "Elevation, Longitude, and Latitude need to be in floating point format";
}
