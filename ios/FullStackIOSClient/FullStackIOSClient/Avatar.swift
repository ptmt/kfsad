//
//  Avatar.swift
//  FullStackIOSClient
//
//  Created by Dmitriy L on 6/13/19.
//  Copyright © 2019 JetBrains. All rights reserved.
//

import SwiftUI
import URLImage

struct Avatar : View {
    var username: String
    var size: Length = 40.0
    var body: some View {
        URLImage(URL(string: "http://i.pravatar.cc/\(size)?u=\(username)")!)
            .frame(width: size, height: size)
            .cornerRadius(4)
            .clipped()
            .shadow(radius: 5)
    }
}

#if DEBUG
struct Avatar_Previews : PreviewProvider {
    static var previews: some View {
        Avatar(username: "Maxime_Nienow")
    }
}
#endif

