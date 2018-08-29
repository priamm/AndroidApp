import android.text.TextUtils
import com.enecuum.androidapp.models.inherited.models.PoANodeUUIDResponse

import com.google.gson.Gson
import junit.framework.Assert.*

import org.junit.Test

import java.util.*

class PoaTests {


    @Test
    @Throws(Exception::class)
    fun check_parser() {
        val gson = Gson()
        val myNodeId: String = UUID.randomUUID().toString()
        if (!TextUtils.isEmpty(myNodeId)) {
            val response = gson.toJson(PoANodeUUIDResponse(nodeId = myNodeId))
            val contains = response.contains(myNodeId)
            assertTrue(contains)
        } else {
            fail()
        }
    }


    @Test
    @Throws(Exception::class)
    fun addition_isNotCorrect() {
        assertEquals("Numbers isn't equals!", 5, 2 + 2)
    }

    object TextUtils {
        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.length == 0
        }
    }
}