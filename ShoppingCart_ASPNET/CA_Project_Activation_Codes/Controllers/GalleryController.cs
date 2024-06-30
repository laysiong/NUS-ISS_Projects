using CA_Project_Activation_Codes.Data;
using CA_Project_Activation_Codes.Models;
using Microsoft.AspNetCore.Mvc;
using MySql.Data;
using MySqlConnector;
using MySqlX.XDevAPI;
using System.Reflection.PortableExecutable;
using System.Runtime.InteropServices;

namespace CA_Project_Activation_Codes.Controllers
{
    public class GalleryController : Controller
    {
        private string? custId;
        private string custTranId;

        public IActionResult Index(string? searchStr)
        {
            
            ISession sessionObject = HttpContext.Session;
            string? usernameInSession = sessionObject.GetString("username");

            string? useridInSession = sessionObject.GetString("userid");
            String SessionID = FindSessionId.GetSessionId(Request, Response);
            custId = useridInSession == null ? SessionID : useridInSession;

            ViewData["LoginStatus"] = usernameInSession == null ? "display" : "hide";
            ViewData["LogoutStatus"] = usernameInSession == null ? "hide" : "display";

            ViewData["username"] = usernameInSession;


            List<Product> products = ProductDAO.GetAllProducts();


            if (string.IsNullOrEmpty(searchStr))
            {
                ViewData["products"] = products;
            }
            else
            {
                ViewData["searchStr"] = searchStr;
                ViewData["products"] = Search(searchStr, products);
            }

            ViewBag.CartItemCount = ProductDAO.getCartCount(custId);
            int cartItemCount = ViewBag.CartItemCount;

            // Get cart count logic (NEED CUST ID)
            //HttpContext.Session.SetInt32("CartItemCount", cartItemCount);

            return View();
        }

        public List<Product> Search(string searchStr, List<Product> products)
        {
            List<Product> searched = new List<Product>();
            foreach (var product in products)
            {
                if (product.ProdName!.ToLower().Contains(searchStr.ToLower()) ||
                    product.Description!.ToLower().Contains(searchStr.ToLower()))
                {
                    searched.Add(product);
                }
            }
            return searched;
        }

        public IActionResult CountCart()
        {
            ISession sessionObject = HttpContext.Session;
            string? useridInSession = sessionObject.GetString("userid");
            String SessionID = FindSessionId.GetSessionId(Request, Response);
            custId = useridInSession == null ? SessionID : useridInSession;

            //Console.WriteLine();
            return Ok(ProductDAO.getCartCount(custId));
        }



        // IF this change to AJAX (Count Carts might to change to AJAX TOO)
        public IActionResult AddCart(string? ProdId)
        {
            //To get id and tranId
            string? useridInSession = HttpContext.Session.GetString("userid");
            string? SessionID = FindSessionId.GetSessionId(Request, Response);
            custId = useridInSession == null ? SessionID : useridInSession;

            string getTranId = baseDAO.getTranID(custId);
            custTranId = useridInSession == null ? custId + "T": getTranId ;
            //Console.WriteLine("Gallery" +"addCart" + custId + "TranId" + custTranId);

            ///////////////////////////// BREAK //////////////////////////////////

            //Console.WriteLine(returnUrl);
            if (string.IsNullOrEmpty(ProdId))
            {
                // Handle the case where ProdId is not provided
                return BadRequest("ProdId is required.");
            }

            ProductDAO.addToCart(ProdId, custId, custTranId);
            return Ok();
        }

    }
}
