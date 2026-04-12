package com.athletiq.app.data.seed

/**
 * Centralized lookup of detailed exercise descriptions used during database seeding.
 *
 * Each entry maps an exercise name (exactly matching the seed JSON) to a concise
 * coaching description covering setup, execution cues, and common mistakes.
 */
object ExerciseDescriptions {

    fun getDescription(exerciseName: String): String? = descriptions[exerciseName]

    private val descriptions: Map<String, String> = mapOf(
        // ── A ────────────────────────────────────────────────────────────────
        "90/90 Hip Stretch" to
            "Sit with your front leg bent 90° in front and rear leg bent 90° behind. Keep your torso tall and lean gently forward over the front shin to stretch the hip capsule. Hold, breathe deeply, and switch sides.",

        "Ab Wheel Rollout" to
            "Kneel on a pad, grip the ab wheel with both hands, and brace your core. Roll the wheel forward as far as you can control without letting your lower back sag, then pull back to the start by contracting your abs.",

        "Assault Bike" to
            "Sit on the air bike with feet on the pedals and hands on the handles. Drive with both your legs and arms in a steady rhythm. The fan resistance increases with effort — keep a sustainable pace unless sprinting.",

        "Assault Bike Calories" to
            "Same as the Assault Bike but your target is a calorie count rather than time. Push hard with both arms and legs — short, all-out bursts burn calories fastest on the air bike.",

        "Assault Bike Cool-Down" to
            "Pedal the air bike at a very easy pace with light arm movement. Keep your heart rate coming down gradually. Focus on deep, relaxed breathing to promote recovery.",

        "Assault Bike Sprint" to
            "Go all-out on the air bike for the prescribed interval. Explode with your legs and pull/push hard with your arms. Stay seated, grip tight, and maintain max effort for the full duration.",

        // ── B ────────────────────────────────────────────────────────────────
        "Banded Pull-Apart" to
            "Hold a light resistance band at shoulder width with arms extended in front. Pull the band apart by squeezing your rear delts and mid-back until the band touches your chest. Return slowly.",

        "Banded Shoulder Dislocates" to
            "Hold a band wide with straight arms in front. Slowly arc both arms overhead and behind your body, keeping elbows locked. Reverse the motion. Use a wider grip if mobility is limited.",

        "Barbell Back Squat" to
            "Place the bar on your upper traps, unrack, and step back. Set feet shoulder-width apart, toes slightly out. Brace your core, push hips back and bend knees to descend to at least parallel. Drive through your whole foot to stand.",

        "Barbell Back Squat (Speed Work)" to
            "Use a lighter load (50-60% 1RM) and focus on explosive speed out of the bottom. Descend under control, then drive up as fast as possible. Reset and brace between each rep.",

        "Barbell Bench Press" to
            "Lie on the bench with eyes under the bar. Grip slightly wider than shoulder width, unrack, and lower the bar to your mid-chest with elbows at roughly 45°. Press back up to lockout. Keep your feet flat and shoulder blades pinched.",

        "Barbell Bench Press (Speed Work)" to
            "Use 50-60% of your max. Lower the bar under control, pause briefly on the chest, then press up as explosively as possible. Focus on bar speed, not grinding.",

        "Barbell Bent-Over Row" to
            "Hinge at the hips with a slight knee bend, torso at roughly 45°. Grip the bar just outside your knees. Pull the bar to your lower chest/upper belly, squeezing your shoulder blades together. Lower with control.",

        "Barbell Close-Grip Bench Press" to
            "Grip the bar with hands about shoulder-width apart. Lower to your lower chest keeping elbows tucked close to your sides. Press up to lockout. This shifts emphasis to the triceps.",

        "Barbell Deadlift" to
            "Stand with feet hip-width apart, bar over mid-foot. Hinge down and grip the bar just outside your legs. Brace hard, then drive through the floor by extending hips and knees together. Keep the bar close to your body. Stand tall at the top.",

        "Barbell Deadlift (Speed Work)" to
            "Use 50-60% of your max. Set up with perfect form, then pull as fast as possible while maintaining a neutral spine. Reset fully between reps — no bouncing.",

        "Barbell Front Rack Lunge" to
            "Clean the bar to a front rack position (elbows high, bar on front delts). Step forward into a lunge, lowering your rear knee toward the floor. Push off the front foot to return. Alternate legs.",

        "Barbell Front Squat" to
            "Rack the bar on your front delts with elbows high. Keep your torso as upright as possible. Squat to full depth, then drive up. The front-loaded position demands core stability and upper back strength.",

        "Barbell Good Morning" to
            "Place the bar on your upper back as in a squat. With a slight knee bend, hinge at the hips and push them back until your torso is near parallel to the floor. Squeeze your glutes and hamstrings to return upright.",

        "Barbell Hang Power Clean" to
            "Start standing with the bar at hip height. Dip by bending your knees slightly, then explosively extend your hips and shrug to propel the bar upward. Catch it in a front rack position with a quarter squat.",

        "Barbell Hip Thrust" to
            "Sit on the floor with your upper back against a bench and a loaded bar over your hips. Plant your feet flat. Drive through your heels and squeeze your glutes to lift your hips until your body forms a straight line from knees to shoulders.",

        "Barbell Hip Thrust (Explosive)" to
            "Set up like a standard hip thrust but drive up as fast and powerfully as possible. Pause briefly at the top with a hard glute squeeze, then lower with control. Keep the movement explosive on every rep.",

        "Barbell Overhead Press" to
            "Stand with the bar at your front delts. Brace your core, squeeze your glutes, and press the bar straight overhead. Move your head through once the bar clears it. Lock out with the bar directly over your midfoot.",

        "Barbell Push Jerk" to
            "From the front rack, dip by bending your knees, then explosively drive upward. As the bar rises, quickly drop under it and catch with arms locked overhead in a partial squat. Stand to full extension.",

        "Barbell Push Press" to
            "Start with the bar at your front delts. Dip by bending your knees slightly, then drive through your legs to generate momentum and press the bar overhead. Lock out at the top.",

        "Barbell Reverse Lunge" to
            "With the bar on your back, step one foot backward and lower your rear knee toward the floor. Keep your front shin mostly vertical. Push off the front foot to return to standing. Alternate legs.",

        "Barbell Rollout" to
            "Load a barbell with round plates. Kneel behind it, grip the bar, and roll it forward by extending your hips and shoulders. Go as far as you can control, then pull back using your abs. Don't let your hips sag.",

        "Barbell Stiff-Leg Deadlift" to
            "Hold the bar at hip height with legs nearly straight (a slight bend is fine). Hinge at the hips, pushing them back, and lower the bar along your legs until you feel a deep hamstring stretch. Drive hips forward to return.",

        "Barbell Z Press" to
            "Sit on the floor with legs straight in front (no back support). Press the bar overhead from your shoulders. This eliminates leg drive and forces strict core and shoulder strength.",

        "Battle Ropes" to
            "Hold one end of the rope in each hand. Create alternating waves by rapidly raising and lowering your arms. Keep your core tight and maintain a slight squat. Go for max intensity during the work interval.",

        "Bicycle Crunches" to
            "Lie face-up with hands behind your head. Bring one knee toward your chest while rotating your opposite elbow to meet it. Alternate sides in a pedaling motion. Keep your lower back pressed to the floor.",

        "Bird Dog" to
            "Start on all fours with a flat back. Simultaneously extend your right arm forward and left leg back until both are in line with your torso. Hold briefly, return, and switch sides. Keep your hips level throughout.",

        "Box Jump Overs" to
            "Face the box, jump up and onto it, then step or jump down the other side. Turn around and repeat. Focus on a soft landing with bent knees each time you land.",

        "Box Jumps" to
            "Stand facing a plyo box. Swing your arms and jump onto the box, landing softly with both feet. Stand fully upright on top, then step down. Reset and repeat.",

        "Box Jumps (Higher Box)" to
            "Same technique as standard box jumps but with a taller box. Drive your knees higher to clear the edge. Only increase height when you can land with confidence and control.",

        "Box Jumps (Max Height)" to
            "Attempt the tallest box you can safely land on. Full arm swing, explosive hip extension, and tuck your knees high. Land softly with stable balance before stepping down.",

        "Broad Jumps" to
            "Stand with feet hip-width. Swing your arms back, hinge your hips, then explode forward and upward. Land softly on both feet with bent knees and absorb the impact. Reset between jumps.",

        "Bulgarian Split Squat" to
            "Place the top of your rear foot on a bench behind you. With your front foot forward, lower into a lunge until your rear knee nearly touches the floor. Push through the front foot to stand. Keep your torso upright.",

        "Burpee Box Jump Overs" to
            "Do a full burpee (chest to floor), then on the way up immediately jump onto and over a box. Step or jump down, turn around, and repeat.",

        "Burpee Box Jumps" to
            "Perform a full burpee, then immediately jump onto a plyo box. Stand tall on top, step down, and repeat. Maintain a fast pace while keeping clean reps.",

        "Burpees" to
            "From standing, drop your chest to the floor, then push up and jump your feet forward. Stand and jump with your hands overhead. Land and immediately go into the next rep. Keep a steady rhythm.",

        // ── C ────────────────────────────────────────────────────────────────
        "Cable Woodchop" to
            "Set a cable at high or low position. Stand sideways, grab the handle with both hands and rotate your torso to pull the cable diagonally across your body. Control the return. Keep your hips stable and let your core do the work.",

        "Cat-Cow Stretch" to
            "On all fours, alternate between arching your back (cow — belly drops, chest lifts) and rounding your spine (cat — tuck chin, push upper back up). Move slowly with your breath to mobilise the entire spine.",

        "Chest-Supported Row" to
            "Lie face-down on an incline bench set to about 30-45°. Let dumbbells hang below. Row both dumbbells up by squeezing your shoulder blades together, then lower with control. This removes momentum cheating.",

        "Chest-to-Bar Pull-Ups" to
            "Perform a pull-up but continue pulling until your chest touches the bar. This requires more lat and mid-back engagement than a chin-over-bar pull-up. Use a slight kip if your programme allows it.",

        "Child's Pose" to
            "From kneeling, sit your hips back toward your heels and extend your arms forward on the floor. Relax your forehead down and breathe deeply. A gentle stretch for the back, shoulders, and hips.",

        "Clapping Push-Ups" to
            "From a push-up position, lower your chest to the floor, then push up explosively so your hands leave the ground. Clap in the air and land with soft elbows. Build up to these — they require solid pressing strength.",

        "Copenhagen Plank" to
            "Lie on your side and place your top foot on a bench. Lift your hips off the ground so your body forms a straight line, with your bottom leg hanging free. Hold. This intensely targets the adductors and obliques.",

        "Couch Stretch" to
            "Kneel with one knee on the ground and press the top of that foot against a wall or bench behind you. Step your other foot forward into a lunge. Squeeze the glute of the rear leg to deepen the hip flexor stretch.",

        // ── D ────────────────────────────────────────────────────────────────
        "Dead Bug" to
            "Lie face-up with arms reaching toward the ceiling and knees bent at 90°. Slowly extend one arm overhead and the opposite leg out straight, keeping your lower back pressed firmly into the floor. Return and switch sides.",

        "Depth Jumps" to
            "Stand on a box, step off, and the moment your feet hit the ground, immediately jump as high as possible. Minimise ground contact time — think 'hot floor'. Land softly on the second landing.",

        "Depth Jumps (Higher Box)" to
            "Same as depth jumps but from a taller box, increasing the drop height and reactive demand. Only progress box height when you can perform clean, fast rebounds without collapsing on landing.",

        "Dragon Flag Negatives" to
            "Lie on a bench and grip behind your head. Raise your body to a vertical position, then slowly lower yourself in a straight line, keeping your core braced. Fight gravity all the way down. Extremely challenging core exercise.",

        "Dumbbell Devil Press" to
            "Start standing with two dumbbells. Drop into a burpee with hands on the dumbbells. From the floor, swing the dumbbells between your legs and snatch or push-press them overhead in one fluid motion. Lower and repeat.",

        "Dumbbell Hang Clean & Press" to
            "Hold dumbbells at your sides. Dip your knees, then explosively shrug and pull the dumbbells to your shoulders (a hang clean). From there, press them overhead. Lower back to the hang position and repeat.",

        "Dumbbell Incline Press" to
            "Set a bench to 30-45°. Press dumbbells from chest level to lockout overhead. Keep your shoulder blades pinched back into the bench and lower with control. Targets the upper chest and front delts.",

        "Dumbbell Jump Squat" to
            "Hold light dumbbells at your sides. Squat down, then explode upward into a jump. Land softly with bent knees and flow straight into the next squat. Keep the dumbbells stable at your sides throughout.",

        "Dumbbell Lateral Raise" to
            "Stand with dumbbells at your sides. Raise both arms out to the sides until they reach shoulder height, leading with your elbows. Lower slowly. Keep a slight bend in your elbows and avoid swinging.",

        "Dumbbell Overhead Press" to
            "Sit or stand with dumbbells at shoulder height, palms facing forward. Press both dumbbells overhead until arms are fully extended. Lower back to shoulder level with control.",

        "Dumbbell Snatches" to
            "Start with a dumbbell on the floor between your feet. Hinge down, grip it, then in one explosive movement drive it overhead, fully extending your hips and arm. Lower and repeat. Alternate arms as prescribed.",

        "Dumbbell Step-Ups" to
            "Hold dumbbells at your sides and face a box or bench. Step up with one foot, driving through the heel to stand tall on top. Step down with control. Complete all reps on one leg or alternate.",

        "Dumbbell Thrusters" to
            "Hold dumbbells at shoulder height. Squat down, then drive up explosively and press the dumbbells overhead in one fluid motion. Lower the dumbbells back to your shoulders as you descend into the next squat.",

        // ── F ────────────────────────────────────────────────────────────────
        "Farmer's Carry" to
            "Pick up heavy dumbbells or kettlebells and walk with them at your sides. Stand tall, keep your shoulders packed, core braced, and take steady steps. Great for grip, core, and total-body stability.",

        "Farmer's Walk" to
            "Same as the farmer's carry — hold heavy weights at your sides and walk a prescribed distance. Maintain an upright posture with shoulders back and down. Don't let the weights pull you into a slouch.",

        "Foam Roll — Adductors & Glutes" to
            "Use a foam roller on your inner thighs and glute muscles. Roll slowly, pausing on tender spots for 20-30 seconds. Keep your breathing steady and relaxed.",

        "Foam Roll — Full Body" to
            "Systematically roll all major muscle groups: quads, hamstrings, glutes, IT band, upper back, and lats. Spend extra time on any sore areas. Slow, deliberate passes work best.",

        "Foam Roll — Glutes & Hamstrings" to
            "Sit on the roller and shift your weight onto one glute at a time. Then extend your leg and roll along the back of your thigh. Pause on tight spots and breathe through the discomfort.",

        "Foam Roll — Lats & Thoracic Spine" to
            "Lie on your side with the roller under your armpit area to roll your lats. Then turn face-up and roll your upper back, crossing your arms over your chest to allow the roller to work the thoracic spine.",

        "Foam Roll — Lower Body" to
            "Roll your quads, hamstrings, glutes, IT band, and calves. Spend 30–60 seconds per area. Apply firm but tolerable pressure and move slowly over each muscle group.",

        "Foam Roll — Quads & Adductors" to
            "Lie face-down with the roller under your thighs to roll your quads. Then shift to one side to target the inner thigh (adductors). Slow passes, pause on knots.",

        "Foam Roll — Quads & IT Band" to
            "Lie face-down on the roller for your quads, rolling from hip to just above the knee. Rotate to your side to target the IT band. This can be intense — control the pressure with your arms.",

        "Foam Roll — Quads, Adductors & Calves" to
            "Roll your quads face-down, shift to your inner thighs for adductors, then sit and place the roller under your calves. Hit each area for 30-60 seconds.",

        "Foam Roll — Quads, Hamstrings & Glutes" to
            "Cover the entire upper leg: face-down for quads, flip over for hamstrings, then sit on the roller for glutes. Pause on tight spots and breathe deeply.",

        "Foam Roll — Thoracic Spine" to
            "Lie face-up with the roller across your upper back. Support your head, lift your hips, and roll from mid-back to the base of your neck. Extend gently over the roller to open up your thoracic spine.",

        "Foam Roll — Upper Back & Lats" to
            "Roll your upper back face-up, then turn to each side to target your lats. Cross your arms to allow the roller to reach between your shoulder blades.",

        "Foam Roll — Upper Back, Lats & Pecs" to
            "Roll upper back and lats as described, then use a ball or roller edge against a wall to release your pec muscles. Press gently and move slowly across the chest.",

        "Foam Roll — Upper Body" to
            "Cover your upper back, lats, rear delts, and the sides of your torso. Lie face-up for the upper back and rotate to each side for the lats. Slow, steady movement.",

        "Football / Pick-Up Game" to
            "Play at a moderate, fun intensity. Focus on movement quality — sprints, changes of direction, and social enjoyment. This serves as active recovery for the week, not max exertion.",

        "Front Rack Kettlebell Lunges" to
            "Hold two kettlebells in a front rack position (handles gripped, bells resting on your forearms). Step forward into a lunge, lowering your rear knee toward the floor. Push off the front foot to return.",

        "Full Body Stretch" to
            "A general stretching routine covering all major muscle groups. Hold each stretch for 30-45 seconds, breathing deeply. Don't bounce — use static, sustained holds to improve flexibility.",

        // ── G ────────────────────────────────────────────────────────────────
        "Goblet Squat" to
            "Hold a kettlebell or dumbbell at your chest with both hands. Squat down keeping your elbows inside your knees and torso upright. Push through your heels to stand. Great for grooving squat depth.",

        "Goblet Squats" to
            "Hold a kettlebell or dumbbell at your chest with both hands. Squat down keeping your elbows inside your knees and torso upright. Push through your heels to stand. Great for grooving squat depth.",

        // ── H ────────────────────────────────────────────────────────────────
        "Hang Power Snatch" to
            "Start standing with the bar at hip height using a wide snatch grip. Dip your knees, then explosively extend your hips and pull the bar overhead in one motion, catching it with straight arms in a partial squat.",

        "Hanging Knee Raise" to
            "Hang from a pull-up bar with straight arms. Curl your knees up toward your chest by contracting your abs. Lower with control — avoid swinging. Pause briefly at the top.",

        "Hanging Knee Raises" to
            "Hang from a pull-up bar with straight arms. Curl your knees up toward your chest by contracting your abs. Lower with control — avoid swinging. Pause briefly at the top.",

        "Hanging Leg Raise" to
            "Hang from a bar and raise straight legs until they are parallel to the floor (or higher). Lower slowly without swinging. Extremely effective for lower abs and hip flexor strength.",

        "Hanging Leg Raises" to
            "Hang from a bar and raise straight legs until they are parallel to the floor (or higher). Lower slowly without swinging. Extremely effective for lower abs and hip flexor strength.",

        // ── K ────────────────────────────────────────────────────────────────
        "Kettlebell Clean & Press" to
            "Swing the kettlebell from between your legs and clean it to the rack position at your shoulder. From there, press it overhead to lockout. Lower back to the rack and then to the hang. Alternate or do one side at a time.",

        "Kettlebell Swing" to
            "Stand over the kettlebell, hinge down to grip it, then hike it back between your legs. Snap your hips forward to drive the bell to chest or eye height. Let it fall back and repeat with a powerful hip hinge.",

        "Kettlebell Swing (Heavy)" to
            "Same as a standard swing but with a heavier bell. Focus on an aggressive hip snap and strong brace at the top. Use two hands and don't let the weight pull you forward.",

        "Kettlebell Swings" to
            "Stand over the kettlebell, hinge down to grip it, then hike it back between your legs. Snap your hips forward to drive the bell up. Control the downswing with your lats and repeat rhythmically.",

        // ── L ────────────────────────────────────────────────────────────────
        "Lacrosse Ball — Pecs & Front Delt" to
            "Place a lacrosse ball between your chest/front shoulder and a wall. Apply gentle pressure and roll slowly to find trigger points. Pause on tender spots for 20-30 seconds, breathing steadily.",

        "Landmine Rotation" to
            "Anchor one end of a barbell in a landmine attachment. Hold the other end with both hands at chest height. Rotate your torso to move the bar in an arc from side to side. Keep your hips stable and core engaged.",

        "Landmine Rotation (Heavy)" to
            "Same as the landmine rotation but with heavier loading. Brace harder and move with control. This challenges your obliques and anti-rotation strength under greater load.",

        "Lat Pulldown" to
            "Sit at a lat pulldown machine, grip the bar wider than shoulder width. Pull the bar down to your upper chest by driving your elbows down and back. Squeeze your lats, then slowly return to full stretch overhead.",

        "Light Jog Cool-Down" to
            "Jog at a very easy pace to bring your heart rate down gradually. Keep your stride relaxed and your breathing rhythmic. This promotes blood flow for recovery after intense work.",

        "L-Sit Hold" to
            "Support yourself on parallettes, dip bars, or the floor with straight arms. Lift your legs until they are parallel to the ground and hold. Demands serious core and hip flexor strength.",

        // ── M ────────────────────────────────────────────────────────────────
        "Meadows Row" to
            "Stand perpendicular to a barbell anchored in a landmine. Stagger your stance and grab the end of the bar with one hand. Row it toward your hip, squeezing your lat hard at the top. Lower with a stretch.",

        "Medicine Ball Chest Pass" to
            "Stand facing a wall or partner holding a med ball at chest height. Explosively push the ball forward using both hands, extending your arms fully. Catch the rebound and repeat with maximal intent.",

        "Medicine Ball Overhead Throw" to
            "Hold a med ball overhead, step forward and slam/throw it as far or as hard as possible. Use your whole body — extend through your hips, core, and arms. Great for developing total-body explosive power.",

        "Medicine Ball Overhead Throw (Heavy)" to
            "Same as the overhead throw but with a heavier ball. Focus on generating force from the ground up through your legs, hips, and core. Release with full extension.",

        "Medicine Ball Slam" to
            "Raise a slam ball overhead, then explosively slam it into the ground in front of you. Hinge at your hips, engage your core, and follow through with your arms. Pick it up and repeat.",

        "Muscle-Ups (or Transition Drill)" to
            "From a dead hang, perform an explosive pull-up and transition over the bar (or rings) into a dip, pressing to full lockout on top. If you can't yet do a full muscle-up, practice the transition portion with a band or low rings.",

        // ── P ────────────────────────────────────────────────────────────────
        "Pallof Press" to
            "Stand sideways to a cable machine with the handle at chest height. Press the handle straight out from your chest and hold — resist the rotation. Bring it back to your chest. This builds anti-rotation core stability.",

        "Pallof Press with Rotation" to
            "Start like a standard Pallof press, but after extending your arms, slowly rotate your torso toward the cable and then away. Fight the pull of the cable through the rotation. Return to centre.",

        "Pec Doorway Stretch" to
            "Stand in a doorway with one arm bent 90° against the frame at shoulder height. Lean gently forward until you feel a stretch across the front of your chest and shoulder. Hold and breathe deeply.",

        "Pendlay Row" to
            "Set up like a bent-over row but with your torso completely parallel to the floor. Pull the bar from the ground to your lower chest explosively, then lower it back to the floor. Reset between each rep.",

        "Pigeon Stretch" to
            "From a kneeling position, bring one shin across your body with the knee out to the side. Extend the other leg straight behind you. Lean forward over the front leg to deepen the glute and hip stretch.",

        "Plank" to
            "Support yourself on your forearms and toes with your body in a straight line from head to heels. Brace your core, squeeze your glutes, and don't let your hips sag or pike up. Hold for the prescribed time.",

        "Plank to Push-Up" to
            "Start in a forearm plank. Press up to a straight-arm plank one hand at a time, then lower back to forearms. Alternate the leading hand each rep. Keep your hips as stable as possible.",

        "Plyometric Push-Ups" to
            "Do a push-up but push off the ground explosively at the top so your hands leave the floor. Land with soft elbows and immediately descend into the next rep. Build pressing power.",

        "Power Clean" to
            "Start with the bar on the floor. Pull it to your mid-thigh, then explosively extend your hips for the second pull. Catch the bar in a front rack position with a quarter squat. Stand to finish.",

        "Power Snatch" to
            "From the floor with a wide grip, pull the bar to your hips, then explosively drive it overhead in one continuous motion. Catch with straight arms in a partial squat. Stand to full extension.",

        "Power Snatch (From Floor)" to
            "Same as the power snatch — start from the floor, execute a fast first pull to the knees, accelerate through the second pull, and catch overhead. Focus on speed and aggression.",

        "Pull-Ups" to
            "Hang from a bar with an overhand grip. Pull yourself up until your chin clears the bar by driving your elbows down. Lower with control to a full dead hang. Use a band for assistance if needed.",

        "Pull-Ups (Bodyweight)" to
            "Standard bodyweight pull-ups with no added weight. Full dead hang at the bottom, chin over the bar at the top. Focus on quality reps and controlled eccentric.",

        "Push Press" to
            "From the front rack, dip your knees slightly, then drive through your legs to push the bar overhead. Use the momentum from your legs to move heavier weight than a strict press.",

        "Push-Ups" to
            "Place your hands shoulder-width apart on the floor. Lower your chest to the ground keeping your body in a straight line. Press back up to full arm extension. Keep your core tight throughout.",

        // ── R ────────────────────────────────────────────────────────────────
        "Ring Dips" to
            "Support yourself on gymnastic rings with arms straight. Lower your body by bending your elbows until your shoulders are below your elbows. Press back up to lockout. Keep the rings stable and turned out at the top.",

        "Ring Rows" to
            "Hang beneath gymnastic rings with your body in a straight line and feet on the floor. Pull your chest up to the rings by squeezing your shoulder blades together. Lower with control. Easier than pull-ups.",

        "Romanian Deadlift" to
            "Hold a barbell at hip height. With a slight knee bend, hinge at the hips and push them back, lowering the bar along your legs until you feel a strong hamstring stretch. Drive your hips forward to return.",

        "Rotational Medicine Ball Throw" to
            "Stand sideways to a wall, hold a med ball at hip height. Rotate explosively and throw the ball into the wall. Catch the rebound and repeat. Drive the rotation from your hips and core.",

        "Rotational Medicine Ball Throw (Heavy)" to
            "Same as the rotational throw but with a heavier ball. Focus on generating maximum rotational power from your hips through your core. Control the catch.",

        "Rowing Machine" to
            "Sit on the rower with feet strapped in. Drive with your legs first, then lean back slightly and pull the handle to your lower chest. Return in reverse order: arms, body, then bend your knees. Maintain a strong, rhythmic pace.",

        "Rowing Sprint" to
            "Same technique as the rowing machine but at maximum effort. Push hard with your legs, pull aggressively, and maintain high stroke rate. Keep your form even when fatiguing.",

        "Russian Twists" to
            "Sit with your knees bent and lean back slightly. Hold a weight at your chest and rotate your torso side to side, touching the weight to the ground beside each hip. Keep your feet off the floor for more challenge.",

        // ── S ────────────────────────────────────────────────────────────────
        "Sandbag Clean" to
            "Straddle a sandbag, squat down and grip it. Explosively stand and pull the bag to your chest/shoulder, hugging it tight. Drop it down and reset. Sandbag cleans build raw, functional power.",

        "Side Plank" to
            "Lie on your side and prop yourself on your forearm. Lift your hips so your body forms a straight line. Hold, keeping your core and glutes engaged. Don't let your hips drop toward the floor.",

        "Side Plank with Hip Dip" to
            "From a side plank position, lower your hips toward the floor, then drive them back up. This adds a dynamic element that targets your obliques more intensely than a static hold.",

        "Single-Arm Dumbbell Push Press" to
            "Hold one dumbbell at shoulder height. Dip your knees, then drive up using your legs to press the dumbbell overhead with one arm. This challenges core stability as you resist lateral bending.",

        "Single-Leg Bounding" to
            "Sprint forward using exaggerated single-leg bounds — push off one foot and drive your knee high before landing on the opposite foot. Cover as much distance per stride as possible. Build running power.",

        "Single-Leg Romanian Deadlift" to
            "Stand on one leg holding a dumbbell or kettlebell. Hinge at the hip, extending the free leg behind you for balance. Lower until your torso is near parallel, then drive back up. Builds single-leg stability.",

        "Ski Erg" to
            "Stand at the ski erg and grip both handles overhead. Pull down powerfully, hinging at the hips and bending your knees slightly. Extend back up and repeat. Uses your lats, core, and triceps.",

        "Slam Ball Throws (Overhead)" to
            "Hold a slam ball overhead, then forcefully throw it to the ground. Use your core, lats, and hips to generate power. Pick it up and repeat. Great for power and stress relief.",

        "Sled Push" to
            "Load a sled and grip the handles at chest or low height. Drive forward with powerful leg strides, keeping your body at an angle and your core braced. Push for the prescribed distance.",

        "Strict Toes-to-Bar" to
            "Hang from a bar and raise your feet all the way up to touch the bar, using only core strength — no kipping or swinging. Lower with control. Extremely demanding on your abs and hip flexors.",

        "Suitcase Carry" to
            "Hold a heavy dumbbell or kettlebell in one hand at your side. Walk the prescribed distance while resisting the lateral bend — stay perfectly upright. This builds oblique and core stability.",

        "Supine Spinal Twist" to
            "Lie on your back and bring one knee across your body toward the opposite side. Extend the same-side arm out and look away from the knee. Hold and breathe to stretch your lower back and thoracic spine.",

        "Supine Toe Taps" to
            "Lie face-up with knees bent at 90° and legs in tabletop position. Slowly lower one toe to the floor, then return. Alternate sides. Keep your lower back flat on the ground throughout.",

        // ── T ────────────────────────────────────────────────────────────────
        "Thrusters (Barbell)" to
            "Hold the barbell in a front rack position. Squat to full depth, then drive up explosively and press the bar overhead in one fluid movement. Lower back to the rack as you descend into the next squat.",

        "Toes-to-Bar" to
            "Hang from a bar and swing your feet up to touch the bar. A kipping motion is allowed — use your lats to initiate the swing. Control the descent to maintain rhythm.",

        "Trap Bar Jump Squat" to
            "Stand inside a loaded trap bar. Squat down, then explosively jump as high as you can. Land softly with bent knees inside the bar. Reset and repeat. The trap bar allows a more natural jump pattern.",

        "Tuck Jumps" to
            "From standing, jump as high as possible and tuck your knees to your chest at the top. Land softly with bent knees and immediately jump again. Fast, explosive, and great for reactive power.",

        "Turkish Get-Up" to
            "Lie on the floor holding a kettlebell overhead with one arm. Using a series of deliberate movements — roll, post, kneel, stand — rise to a full standing position while keeping the weight locked out overhead. Reverse the steps to return.",

        // ── W ────────────────────────────────────────────────────────────────
        "Walking Lunges" to
            "Step forward into a lunge, lowering your rear knee toward the ground. Push off the front foot and step directly into the next lunge with the opposite leg. Keep your torso upright and core braced.",

        "Wall Balls" to
            "Hold a med ball at your chest, squat to depth, then drive up and throw the ball to a target on the wall (usually 10 feet). Catch it on the way down and flow into the next squat.",

        "Wall Slide" to
            "Stand with your back and arms against a wall. Slowly slide your arms overhead keeping contact with the wall at all times. This promotes shoulder mobility and scapular control.",

        "Weighted Medicine Ball Slam" to
            "Same as a medicine ball slam but with a heavier ball. Raise it overhead and slam it into the ground with maximum force. Hinge at the hips, brace your core, and follow through.",

        "Weighted Muscle-Ups" to
            "Perform a muscle-up with added weight (vest or belt). Explosive pull, fast transition over the bar/rings, and press to lockout. Only attempt if you have solid unweighted muscle-ups.",

        "Weighted Plank" to
            "Get into a forearm plank position and have a partner place a weight plate on your upper back. Hold the position with a flat back and braced core. The added load builds serious trunk endurance.",

        "Weighted Pull-Ups" to
            "Add weight using a dip belt, weighted vest, or dumbbell between your feet. Perform pull-ups with full range of motion — dead hang to chin over bar. Control the descent.",

        "World's Greatest Stretch" to
            "Lunge forward, place the same-side elbow to the inside of the front foot, then rotate and reach the same arm to the sky. Step forward into the next lunge. Opens hips, thoracic spine, and ankles in one flow.",

        // ── EMOM / Alternating Minute Labels ─────────────────────────────────
        "Even Min: Burpees" to
            "On even minutes, perform burpees — chest to floor, push up, jump with hands overhead. Move fast but keep reps clean.",

        "Even Min: Chest-to-Bar Pull-Ups" to
            "On even minutes, perform chest-to-bar pull-ups. Pull until your chest touches the bar, then lower to a full hang.",

        "Even Min: Toes-to-Bar" to
            "On even minutes, perform toes-to-bar — hang from the bar and swing your feet up to touch it. Use a kipping rhythm.",

        "Odd Min: Dumbbell Devil Press" to
            "On odd minutes, perform dumbbell devil presses — burpee with dumbbells, then snatch or press them overhead in one fluid motion.",

        "Odd Min: Hang Power Snatch" to
            "On odd minutes, perform hang power snatches. Start with the bar at hip height and explosively drive it overhead.",

        "Odd Min: Power Clean" to
            "On odd minutes, perform power cleans — pull the bar from the floor to the front rack position in one explosive movement.",

        "Min 1: Power Clean & Jerk" to
            "On minute 1, perform power clean and jerks — clean the bar to your shoulders, then dip-and-drive it overhead.",

        "Min 2: Wall Balls" to
            "On minute 2, perform wall balls — squat with a med ball and throw it to a 10-foot target. Catch and repeat.",

        "Min 3: Assault Bike Calories" to
            "On minute 3, burn calories on the assault bike. Go all-out for max calories in the minute."
    )
}
