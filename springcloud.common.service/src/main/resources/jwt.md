#### 官网地址：https://github.com/jwtk/jjwt

## 那么 JWT 是如何签名的呢？让我们用一些易于阅读的伪代码来完成它

1. Assume we have a JWT with a JSON header and body (aka 'Claims') as follows:

```header

{
"alg": "HS256"
}
body

{
"sub": "Joe"
}
```

2. Remove all unnecessary whitespace in the JSON:

```
String header = '{"alg":"HS256"}'
String claims = '{"sub":"Joe"}'
```

3. Get the UTF-8 bytes and Base64URL-encode each:

```
String encodedHeader = base64URLEncode( header.getBytes("UTF-8") )
String encodedClaims = base64URLEncode( claims.getBytes("UTF-8") )
```

4. Concatenate the encoded header and claims with a period character between them:

```
String concatenated = encodedHeader + '.' + encodedClaims
```

5. Use a sufficiently-strong cryptographic secret or private key, along with a signing algorithm of your choice (we'll
   use HMAC-SHA-256 here), and sign the concatenated string:

``` 
Key key = getMySecretKey()
byte[] signature = hmacSha256( concatenated, key )
``` 

6.Because signatures are always byte arrays, Base64URL-encode the signature and append a period character '.' and it to
the concatenated string:

```
String jws = concatenated + '.' + base64URLEncode( signature )
```

And there you have it, the final jws String looks like this:

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.1KP0SsvENi7Uz1oQc07aXTL7kpQG5jBNIybqr60AlD4
```

## 2.签名算法密钥

JWT 规范确定了 12 种标准签名算法——3 种密钥算法和 9 种非对称密钥算法——由以下名称标识：

HS256: 使用 SHA-256 的 HMAC HS384: 使用 SHA-384 的 HMAC HS512: 使用 SHA-512 的 HMAC ES256: ECDSA 使用 P-256 和 SHA-256 ES384: ECDSA
使用 P-384 和 SHA-384 ES512: ECDSA 使用 P-521 和 SHA-512 RS256：使用 SHA-256 的 RSASSA-PKCS-v1_5 RS384: RSASSA-PKCS-v1_5 使用
SHA-384 RS512: RSASSA-PKCS-v1_5 使用 SHA-512 PS256: RSASSA-PSS 使用 SHA-256 和 MGF1 和 SHA-256 PS384: 使用 SHA-384 的 RSASSA-PSS
和使用 SHA-384 的 MGF1 PS512: RSASSA-PSS 使用 SHA-512 和 MGF1 和 SHA-512 这些都在io.jsonwebtoken.SignatureAlgorithm枚举中表示。

这些算法真正重要的是——除了它们的安全属性——JWT 规范 RFC 7518，第 3.2 到 3.5 节 要求（强制）您必须使用对所选算法足够强大的密钥。

1. JWT 规范 RFC 7518：
   [https://datatracker.ietf.org/doc/html/rfc7518#section-3](https://datatracker.ietf.org/doc/html/rfc7518#section-3.2)
2. [https://datatracker.ietf.org/doc/html/rfc7518#section-3.2](https://datatracker.ietf.org/doc/html/rfc7518#section-3.2)

这意味着 JJWT - 一个符合规范的库 - 还将强制您为您选择的算法使用足够强的密钥。如果您为给定算法提供弱密钥，JJWT 将拒绝它并抛出异常。

这不是因为我们想让您的生活变得困难，我们保证！JWT 规范以及因此 JJWT 要求密钥长度的原因是，如果您不遵守算法的强制密钥属性，特定算法的安全模型可能会完全崩溃，实际上根本没有安全性。没有人想要完全不安全的 JWT，对吧？我们也不会。

那么有什么要求呢？

HMAC-SHA 根据RFC 7512 第 3.2 节， JWT HMAC-SHA 签名算法HS256、HS384和HS512需要至少与算法的签名（摘要）长度一样多的密钥。这意味着：

HS256是 HMAC-SHA-256，它会生成 256 位（32 字节）长的摘要，因此HS256 要求您使用至少 32 字节长的密钥。

HS384是 HMAC-SHA-384，它会产生 384 位（48 字节）长的摘要，因此HS384 要求您使用至少 48 字节长的密钥。

HS512是 HMAC-SHA-512，它会生成 512 位（64 字节）长的摘要，因此HS512 要求您使用至少 64 字节长的密钥。

RSA 根据 RFC 7512 第 3.3和3.5节，JWT RSA 签名算法RS256、RS384、RS512、和所有都需要比特的最小密钥长度（也称为 RSA 模比特长度）。小于此值的任何内容（例如 1024
位）都将被拒绝，并带有.PS256PS384PS5122048InvalidKeyException

也就是说，为了与最佳实践保持一致并增加密钥长度以延长安全寿命，JJWT 建议您使用：

至少 2048 位密钥RS256和PS256 至少 3072 位密钥RS384和PS384 至少 4096 位密钥RS512和PS512 这些只是 JJWT 建议而不是要求。JJWT 仅强制执行 JWT 规范要求，对于任何 RSA
密钥，要求是 RSA 密钥（模数）长度（以位为单位）必须 >= 2048 位。

椭圆曲线 JWT 椭圆曲线签名算法ES256、ES384和ES512都需要最小密钥长度（又名椭圆曲线顺序位长度），该长度至少与RFC 7512 第 3.4 节中算法签名的个体 R和S组件一样多。这意味着：

ES256要求您使用至少 256 位（32 字节）长的私钥。

ES384要求您使用至少 384 位（48 字节）长的私钥。

ES512要求您使用至少 512 位（64 字节）长的私钥。

创建安全密钥 如果您不想考虑位长要求或只想让您的生活更轻松，JJWT 提供了io.jsonwebtoken.security.Keys实用程序类，可以为您可能想要使用的任何给定 JWT 签名算法生成足够安全的密钥。

为 JWT 规范中定义的JwtBuilder标准注册声明名称提供了方便的 setter 方法。他们是：

setIssuer：设置iss（发行人）声明 setSubject：设置sub（主题）声明 setAudience：设置aud（观众）声明 setExpiration：设置exp（过期时间）声明 setNotBefore: 设置nbf(
Not Before) 声明 setIssuedAt：设置iat（颁发于）声明 setId：设置jti（JWT ID）声明

