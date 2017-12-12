precision mediump float;

uniform float uTime;
uniform vec2 uResolution;

//float saturate (in float f)
//{
//    return clamp(f,0.0,1.0);
//}
//
//void main()
//{
//    float pi = 3.14;
//    float tao = 6.28;
//    float overbright = 2.0;
//    float armCount = 3.0;
//    float armRot = 1.6;
//
//    vec4 innerColor = vec4(2.0,0.5,0.1,1.0);
//    vec4 outerColor = vec4(0.8,0.6,1.0,1.0);
//    vec4 white = vec4(1.0,1.0,1.0,1.0);
//    float time = uTime;
//
//	vec2 uv = gl_FragCoord.xy / uResolution.xy;
//
//    //constant slow rotation
//    float cost = cos(-time*0.2);
//    float sint = sin(-time*0.2);
//    mat2 trm = mat2 (cost,sint,-sint,cost);
//
//    //scale 0.0-1.0 uv to -1.0-1.0 p
//    vec2 p = uv*2.0 - 1.0;
//    //apply slow rotation
//    p = p * trm;
//
//    //calc distance
//    float d = length(p);
//
//    //build arm rotation matrix
//    float cosr = cos(armRot*sin(armRot*time));
//    float sinr = sin(armRot*cos(armRot*time));
//    mat2 rm = mat2 (cosr,sinr,-sinr,cosr);
//
//    //calc arm rotation based on distance
//    p = mix(p,p * rm,d);
//
//    //find angle to middle
//    float angle = (atan(p.y,p.x)/tao) * 0.5 + 0.5;
//    //add the crinkle
//    angle += sin(-time*5.0+fract(d*d*d)*10.0)*0.004;
//    //calc angle in terms of arm number
//    angle *= 2.0 * armCount;
//    angle = fract(angle);
//    //build arms & wrap the angle around 0.0 & 1.0
//    float arms = abs(angle*2.0-1.0);
//    //sharpen arms
//    arms = pow(arms,10.0*d*d + 5.0);
//    //calc radial falloff
//    float bulk = 1.0 - saturate(d);
//    //create glowy center
//    float core = pow(bulk,9.0);
//    //calc color
//    vec4 color = mix(innerColor,outerColor,d*2.0);
//
//	gl_FragColor = bulk * arms * color + core + bulk*0.25*mix(color,white,0.5);
//    gl_FragColor = (overbright * gl_FragColor);
//}

void main() {
    gl_FragColor = vec4(gl_FragCoord.x / uResolution.x,.0,1.0,1.0);
}