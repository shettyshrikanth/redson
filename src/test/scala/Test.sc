import com.sidemash.redson.JsonArray

import scala.collection.mutable.ListBuffer

val buffer = List.newBuilder[Int]
buffer += 1
buffer += 2
buffer += 3
buffer += 4
val rt = buffer.result()
buffer += 5
buffer += 6
val rt2 = buffer.result()
val  i = rt

val builder = JsonArray.builder()
builder.append(1)
builder.append(2)
builder.append(3)
val dt = builder.build()
