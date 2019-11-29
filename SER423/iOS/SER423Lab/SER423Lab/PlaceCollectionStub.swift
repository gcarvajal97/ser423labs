//
//  PlaceCollectionStub.swift
//  SER423Lab
//
//  Created by Carvajal, Gianni on 11/29/19.
//  Copyright © 2019 Carvajal, Gianni. All rights reserved.
//

import Foundation


class PlaceCollectionStub {
    static var id:Int = 0
        
        var url:String
        
        init(urlString: String){
            self.url = urlString
        }
        
        // used by methods below to send a request asynchronously.
        // asyncHttpPostJson creates and posts a URLRequest that attaches a JSONRPC request as a Data object
        func asyncHttpPostJSON(url: String,  data: Data,
                               completion: @escaping (String, String?) -> Void) {
            
            let request = NSMutableURLRequest(url: NSURL(string: url)! as URL)
            request.httpMethod = "POST"
            request.addValue("application/json",forHTTPHeaderField: "Content-Type")
            request.addValue("application/json",forHTTPHeaderField: "Accept")
            request.httpBody = data
            HTTPsendRequest(request: request, callback: completion)
        }
        
        // sendHttpRequest
        func HTTPsendRequest(request: NSMutableURLRequest,
                             callback: @escaping (String, String?) -> Void) {
            // task.resume() below, causes the shared session http request to be posted in the background
            // (independent of the UI Thread)
            // the use of the DispatchQueue.main.async causes the callback to occur on the main queue --
            // where the UI can be altered, and it occurs after the result of the post is received.
            let task = URLSession.shared.dataTask(with: request as URLRequest) {
                (data, response, error) -> Void in
                if (error != nil) {
                    callback("", error!.localizedDescription)
                } else {
                    DispatchQueue.main.async(execute: {callback(NSString(data: data!,
                                                                         encoding: String.Encoding.utf8.rawValue)! as String, nil)})
                }
            }
            task.resume()
        }
        
        func get(name: String, callback: @escaping (String, String?) -> Void) -> Bool{
            var ret:Bool = false
            PlaceCollectionStub.id = PlaceCollectionStub.id + 1
            do {
                let dict:[String:Any] = ["jsonrpc":"2.0", "method":"get", "params":[name], "id":PlaceCollectionStub.id]
                let reqData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
                self.asyncHttpPostJSON(url: self.url, data: reqData, completion: callback)
                ret = true
            } catch let error as NSError {
                print(error)
            }
            return ret
        }
        
        
        func getNames(callback: @escaping (String, String?) -> Void) -> Bool{
            var ret:Bool = false
            PlaceCollectionStub.id = PlaceCollectionStub.id + 1
            do {
                let dict:[String:Any] = ["jsonrpc":"2.0", "method":"getNames", "params":[ ], "id":PlaceCollectionStub.id]
                let reqData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
                self.asyncHttpPostJSON(url: self.url, data: reqData, completion: callback)
                ret = true
            } catch let error as NSError {
                print(error)
            }
            return ret
        }
        
        func add(place: PlaceDescription, callback: @escaping (String, String?) -> Void) -> Bool{
            var ret:Bool = false
            PlaceCollectionStub.id = PlaceCollectionStub.id + 1
            do {
                let dict:[String:Any] = ["jsonrpc":"2.0", "method":"add", "params":[place.toDict()], "id":PlaceCollectionStub.id]
                let reqData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
                self.asyncHttpPostJSON(url: self.url, data: reqData, completion: callback)
                ret = true
            } catch let error as NSError {
                print(error)
            }
            return ret
        }
        
        func remove(placeName: String, callback: @escaping (String, String?) -> Void) -> Bool{
            var ret:Bool = false
            PlaceCollectionStub.id = PlaceCollectionStub.id + 1
            do {
                let dict:[String:Any] = ["jsonrpc":"2.0", "method":"remove", "params":[placeName], "id":PlaceCollectionStub.id]
                let reqData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
                self.asyncHttpPostJSON(url: self.url, data: reqData, completion: callback)
                ret = true
            } catch let error as NSError {
                print(error)
            }
            return ret
        }

}
