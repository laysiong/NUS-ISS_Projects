namespace CA_Project_Activation_Codes
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container.
            builder.Services.AddControllersWithViews();
            builder.Services.AddSession();


            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (!app.Environment.IsDevelopment())
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();
            app.UseStaticFiles();
            app.UseRouting();
            app.UseAuthorization();

            //use Session
            app.UseSession();

            // use inline middleware to implement dispaly history
            app.Use(async (context, next) =>
            {
                // there can be occasions where users are directed
                // directly to the Gallery page (e.g.via a bookmark)
                // only allow these users to place items on the cart but before
                // they can checkout, we need to get them to login.


                // when it is the first time our client enter our web page
                // And accidentally get into Gallery or cart page directly
                // we will check the cookies whether it has sessionid
                if (context.Request.Path.StartsWithSegments("/Gallery")
                || context.Request.Path.StartsWithSegments("/Cart"))
                {
                    // if no sessionid in the cookies
                    // use the sessionId in Cookies as CustID
                    if (context.Request.Cookies["SessionId"] == null)
                    {
                        string sessionId = System.Guid.NewGuid().ToString().Substring(0, 10);
                        CookieOptions options = new CookieOptions();
                        options.Expires = DateTime.Now.AddDays(30);
                        context.Response.Cookies.Append("SessionId", sessionId, options);
                    }
                }
                await next(context);
            });

            app.MapControllerRoute(
                name: "default",
                pattern: "{controller=Gallery}/{action=Index}/{id?}");

            app.Run();
        }
    }
}