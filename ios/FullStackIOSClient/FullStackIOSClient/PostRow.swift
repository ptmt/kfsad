import SwiftUI
import SharedLib

struct PostRow : View {
    let post: PostWithComments
    let user: User
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                Avatar(username: user.username, size: 25.0).padding(4.0)
               Text(post.post.title).font(.headline)
                
            }
            
            Text(post.post.body)
                .lineLimit(2)
                .font(.caption)
   
            Text("Comments: \(post.comments.count)")
                .font(.caption)
                .color(Color.secondary)
                .padding(EdgeInsets(top: 5, leading: 0, bottom: 0, trailing: 0))
            
             }.padding(5.0)
    }
}

#if DEBUG
func makePost(id: Int = 1) -> Post {
    Post(userId: 1, id: Int32(id), title: "Title \(id) For Preview", body: "By reason of these things, then, the whaling voyage was welcome; the great flood-gates of the wonder-world swung open, and in the wild conceits that swayed me to my purpose, two and two there floated into my inmost soul, endless processions of the whale,")
}

func makeUser() -> User {
    User(id: 1, name: "User Name", username: "Maxime_Nienow", email: "na", address: UserAddress(street: "na", suite: "na", city: "na", zipcode: "na", geo: Geo(lat: "na", lng: "na")), phone: "na", website: "na", company: UserCompany(name: "na", catchPhrase: "na", bs: "na"))
}
struct PostRow_Previews : PreviewProvider {
    static var previews: some View {
        PostRow(
            post: PostWithComments(post: makePost(), comments: []),
            user: makeUser()
        ).previewLayout(.fixed(width: 340, height: 200))
    }
}
#endif
