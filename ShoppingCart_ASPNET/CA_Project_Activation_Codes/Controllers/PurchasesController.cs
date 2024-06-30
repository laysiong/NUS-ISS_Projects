using CA_Project_Activation_Codes.Data;
using CA_Project_Activation_Codes.Models;
using Microsoft.AspNetCore.Mvc;
using MySqlConnector;
using System.Data;

namespace CA_Project_Activation_Codes.Controllers
{
    public class PurchasesController : Controller
    {
        //TimeStamp, ProductID, Random(seed), User
        string? custId;


        public IActionResult Index(string? searchStr, string order)
        {
            // get session username if null --> haven't logged in yet -> return to login page
            ISession sessionObject = HttpContext.Session;
            string? useridInSession = sessionObject.GetString("userid");
            ViewData["LoginStatus"] = useridInSession == null ? "display" : "hide";
            ViewData["LogoutStatus"] = useridInSession == null ? "hide" : "display";
            ViewData["order"] = (order == null || order == "dsc") ? "ascending_order": "descending_order";

            custId = useridInSession;
            //Console.WriteLine("Purchase History : " + custId);

            if (useridInSession == null)
            {
                return RedirectToAction("Index", "Home");
            }

            List<Prod_Purchased> ProdList = PurchaseDAO.GetAllPurchases(custId);

            if (string.IsNullOrEmpty(searchStr))
            {
                if (string.IsNullOrEmpty(order))
                {
                    ViewBag.ProdList = ProdList.OrderByDescending(p => p.Dates);
                }
                else
                {
                    if (order == "asc") { ViewBag.ProdList = ProdList.OrderBy(p => p.Dates); }
                    else ViewBag.ProdList = ProdList.OrderByDescending(p => p.Dates);
                }
            }
            else
            {
                Console.WriteLine("purchase!:" + searchStr);
                ViewData["searchStr"] = searchStr;
                if (string.IsNullOrEmpty(order))
                {
                    ViewBag.ProdList = Search(searchStr, ProdList).OrderByDescending(p => p.Dates);
                }
                else
                {
                    if (order == "asc") { ViewBag.ProdList = Search(searchStr, ProdList).OrderBy(p => p.Dates); }
                    else ViewBag.ProdList = Search(searchStr, ProdList).OrderByDescending(p => p.Dates);
                }
            }
            
            int cartItemCount = HttpContext.Session.GetInt32("CartItemCount") ?? 0;
            ViewBag.CartItemCount = baseDAO.getCartCount(custId);

            return View();
        }

        // find the matched products
        public List<Prod_Purchased> Search(string searchStr, List<Prod_Purchased> products)
        {
            List<Prod_Purchased> searched = new List<Prod_Purchased>();
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


    }
}
