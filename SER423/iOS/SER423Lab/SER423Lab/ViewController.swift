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

import UIKit

class ViewController: UIViewController {
    let jsonStr = """
    {"name" : "ASU-Poly","description" : "Home of ASU's Software Engineering Programs", "category" : "School", "address-title" : "ASU Software Engineering", "address-street" : "7171 E Sonoran Arroyo Mall\\nPeralta Hall 230\\nMesa AZ 85212","elevation" : 1384.0,"latitude" : 33.306388,"longitude" : -111.679121}
    """

    override func viewDidLoad() {
        super.viewDidLoad()
        let decodedJson = convertToDictionary(text: jsonStr)!
        let labels = getLabelsInView(view: self.view)
        let newLocation = PlaceDescription(name: decodedJson["name"] as! String,
                                           description: decodedJson["description"] as! String,
                                           category: decodedJson["category"] as! String,
                                           title: decodedJson["address-title"] as! String,
                                           street: decodedJson["address-street"] as! String,
                                           elevation: decodedJson["elevation"] as! Double,
                                           latitude: decodedJson["latitude"] as! Double,
                                           longitude: decodedJson["longitude"] as! Double)
        let textLabelToDisplay = labels[0]
        textLabelToDisplay.text = newLocation.toString()
        textLabelToDisplay.lineBreakMode = .byWordWrapping
        textLabelToDisplay.numberOfLines = 0
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    func getLabelsInView(view: UIView) -> [UILabel] {
        var results = [UILabel]()
        for subview in view.subviews as [UIView] {
            if let labelView = subview as? UILabel {
                results += [labelView]
            } else {
                results += getLabelsInView(view: subview)
            }
        }
        return results
    }
    
    override func viewDidAppear(_ animated: Bool) {
        NSLog("In viewDidAppear method of first view")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NSLog("In viewWillAppear method of first view")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        NSLog("In viewDidDisappear method of first view")
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("In viewWillDisappear method of first view")
    }
}
