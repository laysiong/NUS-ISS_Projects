using Org.BouncyCastle.Asn1.Ocsp;

namespace CA_Project_Activation_Codes.Data
{
    public class FindSessionId
    {
        public static string? GetSessionId(HttpRequest req, HttpResponse rsp) {
            if (req.Cookies["SessionId"] != null)
            {
                return req.Cookies["SessionId"];
            }
            else
            {
                // if no sessionid in the cookies
                // use guid as a unique number to identify different customer.
                string sessionId = System.Guid.NewGuid().ToString().Substring(0, 10);
                CookieOptions options = new CookieOptions();
                options.Expires = DateTime.Now.AddDays(30);
                rsp.Cookies.Append("SessionId", sessionId, options);
                return sessionId;
            }
        }
    }
}
