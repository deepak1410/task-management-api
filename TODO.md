
### Action items for email service
* Add retry mechanisms or email templates for better UX.
* (GoodToHave) Integrate an actual email provider.
* (GoodToHave) Log failed email sends for audit purposes.

* Make expiry configurable via properties (application.properties)
* Add automatic cleanup of expired tokens (May be by using scheduled job or alternative approach)
* Log email token creation details in dev for easier testing