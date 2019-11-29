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
* @version November 26, 2019
*/

import Foundation

class PlaceDescription {
    var name: String
    var addressTitle: String
    var addressStreet: String
    var category: String
    var description: String
    var elevation: Double
    var longitude: Double
    var latitude: Double
    
    init(name: String, description: String, category: String, title: String,
         street: String, elevation: Double, latitude: Double, longitude: Double) {
        self.name = name
        self.addressTitle = title
        self.addressStreet = street
        self.category = category
        self.description = description
        self.elevation = elevation
        self.longitude = longitude
        self.latitude = latitude
    }
    
    init(dict: [String:Any]) {
        self.name = dict["name"] as? String ?? ""
        self.addressTitle = dict["address-title"] as? String ?? ""
        self.addressStreet = dict["address-street"] as? String ?? ""
        self.category = dict["category"] as? String ?? ""
        self.description = dict["description"] as? String ?? ""
        self.elevation = dict["elevation"] as? Double ?? 0.00
        self.longitude = dict["longitude"] as? Double ?? 0.00
        self.latitude = dict["latitude"] as? Double ?? 0.00
    }
    
    init() {
        self.name = "unknown"
        self.addressTitle = "unknown"
        self.addressStreet = "unknown"
        self.category = "unknown"
        self.description = "unknown"
        self.elevation = 0.0
        self.longitude = 0.0
        self.latitude = 0.0
    }
    
    func toString() -> String{
        return ("""
        Name: \(self.name)
        Address Title: \(self.addressTitle)
        Address Street: \(self.addressStreet)\n
        Category: \(self.category)
        Description: \(self.description)
        Elevation: \(self.elevation)
        Longitude: \(self.longitude)
        Latitude: \(self.longitude)
        """)
    }

    func toJsonString() -> String {
        var jsonStr = "";
        let dict = toDict()
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
        // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }
    
    func toDict() -> [String:Any] {
        let dict:[String:Any] = [
            "name": name,
            "address-title": addressTitle,
            "address-street": addressStreet,
            "category": category,
            "description": description,
            "elevation": elevation,
            "longitude": longitude,
            "latitude": latitude
            ] as [String : Any]
        return dict
    }
    
}
