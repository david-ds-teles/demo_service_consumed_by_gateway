# Demo API
This demo API can be accessed through the gateway at `/api/demo/account/**` and routed to `/api/account/**` just to 
illustrate basic api gateway router and RewritePath filter.

- The demo has security applied to all the endpoints except whitelisted ones configured at `application.yml`.
- The /login will generate a basic jwt token to illustrate authentication/authorization flow.
- Method security is applied using the roles and the `@PreAuthorize` annotation as an example.

> for extra detail check the gateway project.

## Author
- **David Teles** - david.ds.teles@gmail.com