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
* @version November 28, 2019
*/

import Foundation

final class PlaceLibrary {
    
    static let sharedLibrary = PlaceLibrary()
    private var library = [PlaceDescription]()
    
    private init() {}
    
    func getLibrary() -> [PlaceDescription] {
        return PlaceLibrary.sharedLibrary.library
    }
    
    func addPlaceToLibrary(placeToAdd: PlaceDescription) {
        PlaceLibrary.sharedLibrary.library.append(placeToAdd)
    }
    
    func removePlace(placeToRemove: PlaceDescription) {
        let index = find(value: placeToRemove.name)
        print(index)
        if index != nil {
            PlaceLibrary.sharedLibrary.library.remove(at: index!)
        }
        else {
            NSLog("Location not in library. Not removing.")
        }
    }
    
    func find(value searchValue: String) -> Int?
    {
        for (index, value) in PlaceLibrary.sharedLibrary.getLibrary().enumerated()
        {
            if value.name == searchValue {
                return index
            }
        }
    
        return nil
    }
    
    func updateLocation(placeToUpdate: PlaceDescription) {
        let index = find(value: placeToUpdate.name)
        if index != nil {
            PlaceLibrary.sharedLibrary.library[index!] = placeToUpdate
        }
    }
}
