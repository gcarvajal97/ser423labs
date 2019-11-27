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

class SecondaryViewController: UIViewController {

    func performSegueToReturnBack()  {
        if let nav = self.navigationController {
            nav.popViewController(animated: true)
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        let button = self.getLabelsInView(view: self.view)[0]
        button.addTarget(self, action: #selector(dismiss), for: .touchUpInside)
        NSLog("In viewDidAppear method of second view")
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NSLog("In viewWillAppear method of second view")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        NSLog("In viewDidDisappear method of second view")
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("In viewWillDisappear method of second view")
    }
    
    func getLabelsInView(view: UIView) -> [UIButton] {
        var results = [UIButton]()
        for subview in view.subviews as [UIView] {
            if let labelView = subview as? UIButton {
                results += [labelView]
            } else {
                results += getLabelsInView(view: subview)
            }
        }
        return results
    }
    
    @objc func dismiss(sender: UIButton!) {
        self.dismiss(animated: true, completion: nil)
    }
}
